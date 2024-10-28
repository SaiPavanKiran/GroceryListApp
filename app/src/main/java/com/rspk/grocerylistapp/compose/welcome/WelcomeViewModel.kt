package com.rspk.grocerylistapp.compose.welcome

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.constants.getStringFormattedFullDate
import com.rspk.grocerylistapp.firebase.FireBaseAccount
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.model.OwnerData
import com.rspk.grocerylistapp.model.UserInfo
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val fireBaseAccount: FireBaseAccount,
    private val fireBaseDataBase: FireBaseDatabase,
    private val auth:FirebaseAuth
):MainViewModel() {

    fun googleAccount(credential: Credential,googleNavigation : (NavigationRoutes) -> Unit){
        launchCaching{
            currentButtonLoading = CurrentButtonLoading.GOOGLE
            if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(data = credential.data)
                fireBaseAccount.createGoogleAccount(idToken = googleIdTokenCredential.idToken)
                fireBaseDataBase.addUsersInfo(
                    userInfo = UserInfo(
                        userName = googleIdTokenCredential.displayName?: googleIdTokenCredential.id.substringBefore("@"),
                        email = googleIdTokenCredential.id,
                        photoUrl = googleIdTokenCredential.profilePictureUri.toString(),
                        userBio = "Hi, I am ${googleIdTokenCredential.displayName?: "ByIt! User"}",
                        phoneNumber = googleIdTokenCredential.phoneNumber?:"",
                    ),
                    ownerData = OwnerData().copy(userName = googleIdTokenCredential.displayName?: googleIdTokenCredential.id.substringBefore("@")),
                )
                googleNavigation(NavigationRoutes.HomeScreen)
                currentButtonLoading = CurrentButtonLoading.NONE
            }else{
                Log.e( ERROR_TAG , UNEXPECTED_CREDENTIAL )
                currentButtonLoading = CurrentButtonLoading.NONE
            }
        }
    }


    fun createAnonymousAccount(guestUserNavigation : (NavigationRoutes) -> Unit){
        launchCaching{
            currentButtonLoading = CurrentButtonLoading.GUEST
            fireBaseAccount.createAnonymousAccount()
            fireBaseDataBase.addUsersInfo(
                userInfo = UserInfo(
                    userName = "Guest ${auth.currentUser?.uid?.substring(0..5)}",
                    userBio = "I am a Guest",
                    guestAccount = true,
                    accountCreationDate = getStringFormattedFullDate()
                ),
                ownerData = OwnerData().copy(userName = "Guest ${auth.currentUser?.uid?.substring(0..5)}" ),
            )
            guestUserNavigation(NavigationRoutes.HomeScreen)
            currentButtonLoading = CurrentButtonLoading.NONE
        }
    }


    companion object{
        private const val ERROR_TAG = "WelcomeViewModel"
        private const val UNEXPECTED_CREDENTIAL = "Unexpected credential"
    }
}