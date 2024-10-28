package com.rspk.grocerylistapp.firebase.impl

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.constants.getStringFormattedFullDate
import com.rspk.grocerylistapp.firebase.FireBaseStorage
import com.rspk.grocerylistapp.model.UserStorageInfo
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireBaseStorageImpl @Inject constructor(
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) : FireBaseStorage {

    ////////////////////****** Profile Pic Related ********\\\\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun uploadProfilePic(image: Uri): Uri {
        val userId = auth.currentUser?.uid ?: return Uri.EMPTY
        val profilePicRef = storage.reference.child(USER_PROFILE_PICS)
            .child(userId)
        try {
            profilePicRef.putFile(image).await()

            val downloadUrl = profilePicRef.downloadUrl.await()
            return downloadUrl
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload profile pic: ${e.message}")
            return Uri.EMPTY
        }
    }

    override suspend fun deleteUserPhotos() {
        try {
            storage.reference
                .child(USER_PROFILE_PICS)
                .child(auth.currentUser?.uid!!)
                .delete()
        }catch (ex:Exception){
            Log.d(TAG, "Error Near Deleting Profile Pic: ${ex.message}")
        }
    }


    /////////////******** Users Uploaded Bills Related *********\\\\\\\\\\\\\\\\\
    override suspend fun uploadBill(image: Uri): Boolean {
        val currentTime = System.currentTimeMillis()
        val totalSize = calculateUserBucketFilledSize()

        if (totalSize >= 35000000) {
            SnackBarManager.showMessage("Your Storage Bucket Is Filled")
            return false
        }

        return try {
            val uploadTask = storage.reference.child(USER_UPLOADED_IMAGES)
                .child(auth.currentUser?.uid!!)
                .child("${getStringFormattedFullDate()}-$currentTime")
                .putFile(image)

            suspendCoroutine { continuation ->
                uploadTask.addOnSuccessListener {
                    SnackBarManager.showMessage("Uploaded Successfully")
                    continuation.resume(true)
                }.addOnFailureListener { exception ->
                    SnackBarManager.showMessage(exception.message.toString())
                    Log.d(TAG, "Error While Uploading Bill: ${exception.message}")
                    continuation.resume(false)
                }
            }
        } catch (ex: Exception) {
            Log.d(TAG, "Error While Uploading Bill: ${ex.message}")
            false
        }
    }


    override suspend fun calculateUserBucketFilledSize(): Float {
        return suspendCancellableCoroutine { continuation ->
            try {
                storage.reference.child(USER_UPLOADED_IMAGES)
                    .child(auth.currentUser?.uid!!)
                    .listAll()
                    .addOnSuccessListener { listResult ->
                        var totalSize = 0f
                        val files = listResult.items

                        var remainingItems = files.size
                        if(files.isNotEmpty()){
                            files.forEach { file ->
                                file.metadata
                                    .addOnSuccessListener {
                                        totalSize += it.sizeBytes
                                        remainingItems--
                                        if (remainingItems == 0) continuation.resume(totalSize)
                                    }
                                    .addOnFailureListener {
                                        continuation.resumeWithException(it)
                                    }
                            }
                        }else{
                            continuation.resume(totalSize)
                        }
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }

            }catch (ex:Exception){
                Log.d(TAG, "Error While Calculating Bucket Size: ${ex.message}")
                continuation.resumeWithException(ex)
            }
        }
    }


    override suspend fun downloadImage(context: Context, fileName: String, imageUri:String):Boolean{
        try {
            val bytes = storage.getReferenceFromUrl(imageUri)
                .getBytes(Long.MAX_VALUE)
                .await()
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/")
            }

            val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

            return uri?.let {
                resolver.openOutputStream(it)?.use { outputStream  ->
                    outputStream.write(bytes)
                    outputStream.flush()
                    true
                } ?: false
            } ?: false
        }catch (ex:Exception){
            Log.d(TAG, "Error While Downloading Image: ${ex.message}")
            return false
        }
    }


    override suspend fun deleteImage(imageUri: String):Boolean=
        try {
            suspendCancellableCoroutine { continuation ->
                storage.getReferenceFromUrl(imageUri)
                    .delete()
                    .addOnSuccessListener {
                        continuation.resume(true)
                    }
                    .addOnFailureListener {
                        continuation.resume(false)
                    }
            }
        }catch (ex:Exception){
            Log.d(TAG, "Error While Deleting Image: ${ex.message}")
            false
        }


    override suspend fun deleteAllImages(){
        try{
            storage.reference.child(USER_UPLOADED_IMAGES)
                .child(auth.currentUser?.uid!!)
                .listAll()
                .await()
                .items
                .forEach {
                    it.delete().await()
                }
        }catch (ex:Exception){
            Log.d(TAG, "Error While Deleting All Images: ${ex.message}")
        }
    }

    override fun getAllUserUploadedImages(): Flow<List<UserStorageInfo>?> =
        callbackFlow {
            try {
                storage.reference.child(USER_UPLOADED_IMAGES)
                    .child(auth.currentUser?.uid!!)
                    .listAll()
                    .addOnSuccessListener { listResult ->
                        val files = listResult.items
                        launch {
                            trySend(getAllPhotos(files)).isSuccess
                        }
                    }
                    .await()
            } catch (ex: Exception) {
                Log.d("TesTingProgress", ex.message.toString())
            }
            awaitClose()
        }

    private suspend fun getAllPhotos(files: List<StorageReference>): List<UserStorageInfo> = coroutineScope {
        try {
            val deferredResults = files.map { data ->
                async {
                    val metadata = data.metadata.await()
                    val uri = data.downloadUrl.await()

                    UserStorageInfo(
                        uri = uri,
                        fileName = metadata.name ?: "",
                        size = metadata.sizeBytes,
                        type = metadata.contentType ?: ""
                    )
                }
            }
            deferredResults.awaitAll()
        }catch (ex:Exception){
            Log.d(TAG, "Error While Getting All Images: ${ex.message}")
            emptyList()
        }
    }

    companion object {
        private const val TAG = "StorageRelatedError"

        private const val USER_UPLOADED_IMAGES = "user_uploaded_images"
        private const val USER_PROFILE_PICS = "user_profile_pics"
    }
}