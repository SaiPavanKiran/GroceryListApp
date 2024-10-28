package com.rspk.grocerylistapp.firebase

import com.google.firebase.auth.FirebaseUser

interface FireBaseAccount {
    val hasUser: Boolean
    val currentUser: String?
    val isAnonymousUser: Boolean?
    suspend fun createAnonymousAccount()
    suspend fun createEmailAccount(email: String, password: String)
    suspend fun signInWithEmailAccount(email: String, password: String)
    suspend fun createGoogleAccount(idToken:String)
    suspend fun forgotPassword(email: String):Boolean

    suspend fun linkToEmailAccount(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteAccount()
    suspend fun linkWithGoogleAccount(idToken: String)
    suspend fun reAuthenticateEmailAccount(email: String, password: String):Boolean
}