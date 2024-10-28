package com.rspk.grocerylistapp.compose.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.firebase.FireBaseAccount
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.firebase.FireBaseStorage
import com.rspk.grocerylistapp.model.UserInfo
import com.rspk.grocerylistapp.model.UserInfoUpdate
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fireBaseAccount: FireBaseAccount,
    private val fireBaseStorage: FireBaseStorage,
    private val fireBaseDatabase: FireBaseDatabase
) : MainViewModel() {

    var currentUserData by mutableStateOf<UserInfo?>(null)

    init {
        getUserData()
    }

    private fun getUserData() {
        launchCaching {
            currentUserData = fireBaseDatabase.getUserInfo()
        }
    }

    fun uploadUserData(userInfo: UserInfoUpdate,needToStoreUserImage:Boolean) {
        launchCaching {
            try {
                currentButtonLoading = CurrentButtonLoading.USER_PROFILE_UPDATE
                SnackBarManager.showMessage("Please Wait till Updating User Data")
                var imageUri:Uri? = Uri.EMPTY
                if(needToStoreUserImage) {
                    imageUri = fireBaseStorage.uploadProfilePic(Uri.parse(userInfo.photoUrl))
                }
                fireBaseDatabase.updateUsersInfo(
                    UserInfoUpdate(
                        userName = userInfo.userName,
                        userBio = userInfo.userBio,
                        phoneNumber = userInfo.phoneNumber,
                        email = userInfo.email,
                        photoUrl = imageUri.toString().ifEmpty { userInfo.photoUrl  }
                    )
                )
                currentButtonLoading = CurrentButtonLoading.NONE
            }catch (ex:Exception){
                Log.d("ImageUri", ex.toString())
            }
        }
    }

    fun signOut(navigate: (NavigationRoutes) -> Unit) {
        launchCaching {
            currentButtonLoading = CurrentButtonLoading.LOGOUT
            if(fireBaseAccount.isAnonymousUser == true) {
                awaitAll(
                    async { fireBaseDatabase.deleteAccountData() },
                    async { fireBaseStorage.deleteUserPhotos() },
                    async { fireBaseStorage.deleteAllImages() }
                )
            }
            fireBaseAccount.signOut()
            currentButtonLoading = CurrentButtonLoading.NONE
            navigate(NavigationRoutes.WelcomeScreen)
        }
    }
}