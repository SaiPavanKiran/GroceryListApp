package com.rspk.grocerylistapp.firebase.impl

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rspk.grocerylistapp.firebase.FireBaseAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FireBaseAccountImpl @Inject constructor(
    private val auth: FirebaseAuth
) :FireBaseAccount {

    override val currentUser = auth.uid

    override val hasUser :Boolean = auth.currentUser != null

    override val isAnonymousUser = auth.currentUser?.isAnonymous

    override suspend fun createAnonymousAccount(){
        auth.signInAnonymously().await()
    }

    override suspend fun createEmailAccount(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInWithEmailAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun createGoogleAccount(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    override suspend fun linkToEmailAccount(email: String, password: String){
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }


    override suspend fun linkWithGoogleAccount(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.currentUser!!.linkWithCredential(credential).await()
    }


    override suspend fun reAuthenticateEmailAccount(email: String, password: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val credential = EmailAuthProvider.getCredential(email, password)
            auth.currentUser!!.reauthenticate(credential)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun forgotPassword(email: String):Boolean {
        return suspendCancellableCoroutine { continuation ->
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun signOut(){
        try {
            if(auth.currentUser!!.isAnonymous)
                auth.currentUser!!.delete().await()
            else
                auth.signOut()
        }catch (ex:Exception){
            Log.e("TesTingProgress", "Error occurred: ${ex.message}", ex)
        }
    }

    override suspend fun deleteAccount(){
        auth.currentUser!!.delete().await()
    }
}