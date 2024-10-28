package com.rspk.grocerylistapp.firebase

import android.content.Context
import android.net.Uri
import com.rspk.grocerylistapp.model.UserStorageInfo
import kotlinx.coroutines.flow.Flow

interface FireBaseStorage {

    //Profile Pic Related
    suspend fun uploadProfilePic(image: Uri): Uri
    suspend fun deleteUserPhotos()

    //User Uploaded Bills
    suspend fun uploadBill(image: Uri):Boolean
    suspend fun calculateUserBucketFilledSize(): Float
    fun getAllUserUploadedImages(): Flow<List<UserStorageInfo>?>
    suspend fun downloadImage(context: Context, fileName: String, imageUri: String): Boolean
    suspend fun deleteImage(imageUri: String):Boolean
    suspend fun deleteAllImages()
}