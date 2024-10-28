package com.rspk.grocerylistapp.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.common.snackbar.SnackBarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class NavigationState(
    val navController: NavHostController,
    val snackBarHostState: SnackbarHostState,
    val coroutineScope: CoroutineScope,
    snackBarManager: SnackBarManager = SnackBarManager
){
    init {
        coroutineScope.launch {
            snackBarManager.snackBarMessages.filterNotNull().collect { snackBarMessage ->
                snackBarHostState.showSnackbar(message = snackBarMessage.toMessage(navController.context))
                snackBarManager.clearSnackBarState()
            }
        }
    }

    fun navigate(route:NavigationRoutes){
        navController.navigate(route){
            popUpTo(route){
                inclusive = false
            }
            launchSingleTop = true
        }
    }


    fun clearAndNavigate(route: NavigationRoutes) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

}