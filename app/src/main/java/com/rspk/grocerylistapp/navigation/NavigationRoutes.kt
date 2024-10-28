package com.rspk.grocerylistapp.navigation

import com.rspk.grocerylistapp.model.GroceryItemDetails
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(){

    @Serializable
    data object WelcomeScreen:NavigationRoutes()

    @Serializable
    data class LoginScreen( val accountDeletion:Boolean = false,val email:String = "" ):NavigationRoutes()

    @Serializable
    data class SignUpScreen(val continueWithEmail:Boolean = false):NavigationRoutes()

    @Serializable
    data object CommonSigning:NavigationRoutes()

    @Serializable
    data object HomeScreen:NavigationRoutes()

    @Serializable
    data class CartScreen(
        val month:String = "",
        val showMenu:Boolean = false
    ):NavigationRoutes()

    @Serializable
    data class AnalyticsScreen(
        val currentComposeState:Boolean = false
    ):NavigationRoutes()

    @Serializable
    data object ProfileScreen:NavigationRoutes()

}