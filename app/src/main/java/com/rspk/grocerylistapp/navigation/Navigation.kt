package com.rspk.grocerylistapp.navigation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomScaffold
import com.rspk.grocerylistapp.common.modifier.signingNavigationModifier
import com.rspk.grocerylistapp.compose.analytics.AnalyticsScreen
import com.rspk.grocerylistapp.compose.cartscreen.CartScreen
import com.rspk.grocerylistapp.compose.common_signing.CommonSigning
import com.rspk.grocerylistapp.compose.home.HomeScreen
import com.rspk.grocerylistapp.compose.login.LoginScreen
import com.rspk.grocerylistapp.compose.sign_up.SignUpScreen
import com.rspk.grocerylistapp.compose.welcome.WelcomeScreen
import com.rspk.grocerylistapp.compose.profile.ProfileScreen
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val navigationState = rememberNavigationState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = navigationState.snackBarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                        containerColor = colorResource(id = R.color.snack_bar_container_color),
                        contentColor = colorResource(id = R.color.snack_bar_content_color),
                    )
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navigationState.navController,
            startDestination = navigationViewModel.currentRoute(),
        ) {
            this.appNavigationGraph(
                navigationState = navigationState
            )
        }
    }
}



@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) =
    remember(navController,snackBarHostState,coroutineScope){
        NavigationState(navController = navController, snackBarHostState = snackBarHostState, coroutineScope = coroutineScope )
    }



fun NavGraphBuilder.appNavigationGraph(
    navigationState: NavigationState,
){
    composable<NavigationRoutes.WelcomeScreen> {
        WelcomeScreen(
            clearAndNavigate = { route -> navigationState.clearAndNavigate(route)},
            navigate = { route -> navigationState.navigate(route) }
        )
    }

    composable<NavigationRoutes.LoginScreen> { backStackEntry ->
        val backStackEntryValues = backStackEntry.toRoute<NavigationRoutes.LoginScreen>()
        val inputEmail = backStackEntryValues.email
        val accountDeletion = backStackEntryValues.accountDeletion
        LoginScreen(
            loginNavigation = { route -> navigationState.clearAndNavigate(route)},
            inputEmail = inputEmail,
            activateDeleteAccount = accountDeletion,
            modifier = Modifier.signingNavigationModifier(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
    }

    composable<NavigationRoutes.SignUpScreen> { backStackEntry ->
        val backStackEntryValues = backStackEntry.toRoute<NavigationRoutes.SignUpScreen>()
        val continueWithEmail = backStackEntryValues.continueWithEmail
        SignUpScreen(
            signUpNavigation = { route ->
                navigationState.clearAndNavigate(route)
            },
            continueWithEmail = continueWithEmail,
            modifier = Modifier.signingNavigationModifier(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
    }

    composable<NavigationRoutes.CommonSigning> {
        CommonSigning(
            navigate = { navigationRoutes ->
                navigationState.clearAndNavigate(navigationRoutes)
            }
        )
    }

    composable<NavigationRoutes.HomeScreen> {
        val currentRoute = navigationState.navController.currentBackStackEntryAsState().value?.destination?.route?.substringAfterLast(".")
        CustomScaffold(
            content = { itemChange ,listChange,appProposedList ->
                HomeScreen(
                    userCardNavigation = { navigationRoutes ->
                        navigationState.navigate(navigationRoutes)
                    },
                    onCurrentItemsFilteringStringChange = itemChange,
                    onCurrentListChange = listChange,
                    proposedList = appProposedList
                )
            },
            bottomBarNavigation = { navigationRoutes ->
            navigationState.navigate(navigationRoutes) },
            currentRoute = currentRoute ?: "",
        )
    }

    composable<NavigationRoutes.CartScreen> { backStackEntry ->
        val currentRoute = navigationState.navController.currentBackStackEntryAsState().value?.destination?.route?.substringAfterLast(".")?.substringBefore("?")
        val backStackEntryValues = backStackEntry.toRoute<NavigationRoutes.CartScreen>()
        val month = backStackEntryValues.month
        val showMenu = backStackEntryValues.showMenu
        val currentMonth = if(month != "") month else SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(Date())
        CustomScaffold(
            content = { _,_,_ ->
                CartScreen(
                    currentMonth = currentMonth,
                    showMenu = showMenu
                )
            },
            bottomBarNavigation = { navigationRoutes ->
                navigationState.navigate(navigationRoutes) },
            currentRoute = currentRoute ?: "",
        )
    }


    composable<NavigationRoutes.AnalyticsScreen> { backStackEntry ->
        val currentRoute = navigationState.navController.currentBackStackEntryAsState().value?.destination?.route?.substringAfterLast(".")
        val boolean = backStackEntry.toRoute<NavigationRoutes.AnalyticsScreen>().currentComposeState
        CustomScaffold(
            content = { _,_,_ ->
                AnalyticsScreen(
                    currentComposeState = boolean
                )
            },
            bottomBarNavigation = { navigationRoutes ->
                navigationState.navigate(navigationRoutes) },
            currentRoute = currentRoute?.substringBefore("?") ?: "",
        )
    }
    composable<NavigationRoutes.ProfileScreen> {
        val currentRoute = navigationState.navController.currentBackStackEntryAsState().value?.destination?.route?.substringAfterLast(".")
        CustomScaffold(
            content = { _,_,_ ->
                ProfileScreen(
                    clearAndNavigate = { navigationRoute ->
                        navigationState.clearAndNavigate(navigationRoute)
                    },
                    navigate = { navigationRoutes ->
                        navigationState.navigate(navigationRoutes)
                    }
                )
            },
            bottomBarNavigation = { navigationRoutes ->
                navigationState.navigate(navigationRoutes) },
            currentRoute = currentRoute ?: "",
        )
    }
}