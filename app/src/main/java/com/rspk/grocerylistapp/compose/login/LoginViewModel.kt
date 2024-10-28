package com.rspk.grocerylistapp.compose.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.firebase.FireBaseAccount
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.firebase.FireBaseStorage
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fireBaseAccount: FireBaseAccount,
    private val fireBaseDatabase: FireBaseDatabase,
    private val fireBaseStorage: FireBaseStorage,
    private val auth:FirebaseAuth
):MainViewModel(){

    var reAuthenticateEmail by mutableStateOf(false)
    var doesForgotEmailSent by mutableStateOf(false)

    fun login(email:String,password:String,loginNavigation:(NavigationRoutes)->Unit){
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.LOGIN
            fireBaseAccount.signInWithEmailAccount(email,password)
            fireBaseDatabase.updateLastLoginDate()
            loginNavigation(NavigationRoutes.HomeScreen)
            currentButtonLoading = CurrentButtonLoading.NONE
        }
    }

    fun forgotPassword(email: String){
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.FORGOT_PASSWORD
            doesForgotEmailSent = fireBaseAccount.forgotPassword(email)
            currentButtonLoading = CurrentButtonLoading.NONE
            delay(2000L)
            doesForgotEmailSent = false
        }
    }
    fun reAuthenticateUser(email:String,password:String){
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.LOGIN
            reAuthenticateEmail  = fireBaseAccount.reAuthenticateEmailAccount(email,password)
            currentButtonLoading = CurrentButtonLoading.NONE
        }
    }

    fun deleteAccount(loginNavigation: (NavigationRoutes) -> Unit){
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.DELETE_ACCOUNT
            awaitAll(
                async { fireBaseDatabase.deleteAccountData() },
                async { fireBaseStorage.deleteUserPhotos() }
            )
            fireBaseAccount.deleteAccount()
            currentButtonLoading = CurrentButtonLoading.NONE
            reAuthenticateEmail = false
            loginNavigation(NavigationRoutes.WelcomeScreen)
        }
    }
}