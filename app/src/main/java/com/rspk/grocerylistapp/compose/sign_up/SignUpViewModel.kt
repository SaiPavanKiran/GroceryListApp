package com.rspk.grocerylistapp.compose.sign_up

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.constants.getStringFormattedFullDate
import com.rspk.grocerylistapp.firebase.FireBaseAccount
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.firebase.FireBaseStorage
import com.rspk.grocerylistapp.model.OwnerData
import com.rspk.grocerylistapp.model.UserInfo
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val fireBaseAccount: FireBaseAccount,
    private val fireBaseDataBase: FireBaseDatabase,
    private val fireBaseStorage: FireBaseStorage
):MainViewModel() {

    private val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
    private val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*(),.?\":{}|<>])(?=\\S+$).{8,20}$".toRegex()

    fun conditionsMetCheck(name:String,email:String,password:String,rePassword:String):Boolean {
        var isValid = true
        when{
            !email.matches(emailRegex) -> {
                SnackBarManager.showMessage("Invalid Email")
                isValid = false
            }
            !password.matches(passwordRegex) -> {
                SnackBarManager.showMessage("Password must be between 8 and 20 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
                isValid = false
            }
            password != rePassword -> {
                SnackBarManager.showMessage("Passwords do not match")
                isValid = false
            }
        }
        return isValid
    }

    fun signUp(name:String, email:String, password:String, navigate:(NavigationRoutes) -> Unit){
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.SIGN_IN
            fireBaseAccount.createEmailAccount(email,password)
            fireBaseDataBase.addUsersInfo(UserInfo(
                userName = name.ifEmpty { email.substringBefore("@") },
                email = email,
                userBio = "Hi, I am  ${name.ifEmpty { email.substringBefore("@") }}"
            ),
                ownerData = OwnerData(
                    userName = name.ifEmpty { email.substringBefore("@") }
                )
            )
            navigate(NavigationRoutes.HomeScreen)
            currentButtonLoading = CurrentButtonLoading.NONE
            SnackBarManager.showMessage("Sign Up Successful")
        }
    }

    fun continueWithEmail(name: String,email: String, password: String, navigate:(NavigationRoutes) -> Unit){
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.SIGN_IN
            fireBaseAccount.linkToEmailAccount(email,password)
            fireBaseDataBase.addUsersInfo(UserInfo(
                userName = name.ifEmpty { email.substringBefore("@") },
                email = email,
                userBio = "Hi, I am  ${name.ifEmpty { email.substringBefore("@") }}"
            ),
                ownerData = OwnerData(
                    userName = name.ifEmpty { email.substringBefore("@") }
                )
            )
            navigate(NavigationRoutes.HomeScreen)
            currentButtonLoading = CurrentButtonLoading.NONE
            SnackBarManager.showMessage("Sign In Successful")
        }
    }


    fun continueWithGoogle(credential: Credential, googleNavigation : (NavigationRoutes) -> Unit){
        launchCaching{
            currentButtonLoading = CurrentButtonLoading.GOOGLE
            if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(data = credential.data)
                fireBaseAccount.linkWithGoogleAccount(idToken = googleIdTokenCredential.idToken)
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
                Log.e("TesTingProgress" , "Unexpected credential")
                currentButtonLoading = CurrentButtonLoading.NONE
            }
        }
    }
}