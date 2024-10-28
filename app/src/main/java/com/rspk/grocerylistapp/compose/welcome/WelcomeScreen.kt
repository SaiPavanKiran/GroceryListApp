package com.rspk.grocerylistapp.compose.welcome

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.GoogleButton
import com.rspk.grocerylistapp.common.logos.GoogleLogo
import com.rspk.grocerylistapp.common.modifier.welcomeScreenButtonModifier
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.navigation.NavigationRoutes

@Composable
fun WelcomeScreen(
    navigate: (NavigationRoutes) -> Unit,
    clearAndNavigate: (NavigationRoutes) -> Unit,
    welcomeViewModel: WelcomeViewModel = hiltViewModel(),
    configuration: Configuration = LocalConfiguration.current
) {
    if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        WelcomeScreenPortraitContent(
            welcomeViewModel = welcomeViewModel,
            navigate = navigate,
            clearAndNavigate = clearAndNavigate
        )
    }else if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        WelcomeScreenLandscapeContent(
            welcomeViewModel = welcomeViewModel,
            navigate = navigate,
            clearAndNavigate = clearAndNavigate
        )
    }
}

@Composable
fun WelcomeScreenPortraitContent(
    welcomeViewModel: WelcomeViewModel,
    navigate: (NavigationRoutes) -> Unit,
    clearAndNavigate: (NavigationRoutes) -> Unit,
){
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = dimensionResource(id = R.dimen.padding_20)),
    ) {
        GreetingsWIthImage()
        Spacer(modifier = Modifier.weight(0.5f))
        ConnectButtons(
            clearAndNavigate = clearAndNavigate,
            navigate = navigate,
            welcomeViewModel = welcomeViewModel
        )
        Spacer(modifier = Modifier.weight(0.4f))
    }
}

@Composable
fun WelcomeScreenLandscapeContent(
    welcomeViewModel: WelcomeViewModel,
    navigate: (NavigationRoutes) -> Unit,
    clearAndNavigate: (NavigationRoutes) -> Unit,
){
    Row{
       GreetingsWIthImage(
           modifier = Modifier.weight(1f)
       )
        ConnectButtons(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            clearAndNavigate = clearAndNavigate ,
            navigate = navigate,
            welcomeViewModel = welcomeViewModel
        )
    }
}