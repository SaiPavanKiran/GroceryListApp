package com.rspk.grocerylistapp.compose.common_signing

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.modifier.transparentButtonColors
import com.rspk.grocerylistapp.common.modifier.welcomeEmailButtonColors
import com.rspk.grocerylistapp.compose.login.LoginScreen
import com.rspk.grocerylistapp.compose.sign_up.SignUpScreen
import com.rspk.grocerylistapp.navigation.NavigationRoutes

@Composable
fun CommonSigning(
    configuration: Configuration = LocalConfiguration.current,
    navigate: (NavigationRoutes) -> Unit
){
    var login by rememberSaveable { mutableStateOf(true) }

    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitLayout(onLoginChange = { login = it }, login = login) {
            if (login) {
                LoginScreen(
                    modifier = it,
                    loginNavigation = navigate
                )
            } else {
                SignUpScreen(
                    modifier = it,
                    signUpNavigation = navigate
                )
            }
        }
    } else {
        LandScapeLayout(onLoginChange = { login = it }, login = login) {
            if (login) {
                LoginScreen(
                    modifier = it,
                    loginNavigation = navigate
                )
            } else {
                SignUpScreen(
                    modifier = it,
                    signUpNavigation = navigate
                )
            }
        }
    }
}


@Composable
fun PortraitLayout(
    onLoginChange:(Boolean) -> Unit,
    login:Boolean = true,
    content:@Composable (Modifier) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row (
            modifier = Modifier
                .weight(0.35f)
                .fillMaxWidth(0.75f)
                .padding(bottom = dimensionResource(id = R.dimen.padding_40)),
            verticalAlignment = Alignment.Bottom
        ){
            LoginChangeButton(
                onClick = { onLoginChange(true) },
                buttonColor = !login
            )
            LoginChangeButton(
                text = "Sign-Up",
                onClick = { onLoginChange(false) },
                buttonColor = login
            )
        }
        content(Modifier.weight(0.50f))
        Spacer(modifier = Modifier.weight(0.15f))
    }
}

@Composable
fun LandScapeLayout(
    onLoginChange:(Boolean) -> Unit,
    login:Boolean = true,
    content:@Composable (Modifier) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row (
            modifier = Modifier
                .weight(0.40f)
                .fillMaxWidth(0.75f)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_40)),
            verticalAlignment = Alignment.Bottom
        ){
            LoginChangeButton(
                onClick = { onLoginChange(true) },
                buttonColor = !login
            )
            LoginChangeButton(
                text = "Sign-Up",
                onClick = { onLoginChange(false) },
                buttonColor = login
            )
        }
        content(
            Modifier
            .weight(0.60f)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_40))
        )
    }
}
@Composable
fun RowScope.LoginChangeButton(
    text:String = "Sign-In",
    onClick:() -> Unit,
    buttonColor:Boolean
){
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f),
        colors = ButtonDefaults.let {
            if (buttonColor) it.welcomeEmailButtonColors()
            else it.transparentButtonColors()
        }

    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical =dimensionResource(id = R.dimen.padding_5)))
    }
}