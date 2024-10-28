package com.rspk.grocerylistapp.navigation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.firebase.FireBaseAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val fireBaseAccount: FireBaseAccount,
):ViewModel() {

    fun currentRoute():NavigationRoutes =
        if (fireBaseAccount.hasUser){
            NavigationRoutes.HomeScreen
        }else{
            NavigationRoutes.WelcomeScreen
        }
}