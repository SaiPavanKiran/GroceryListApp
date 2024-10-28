package com.rspk.grocerylistapp.compose.welcome

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.GoogleButton
import com.rspk.grocerylistapp.common.logos.GoogleLogo
import com.rspk.grocerylistapp.common.modifier.welcomeEmailButtonColors
import com.rspk.grocerylistapp.common.modifier.welcomeTextButtonColors
import com.rspk.grocerylistapp.common.modifier.welcomeScreenButtonModifier
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.navigation.NavigationRoutes

@Composable
fun GreetingsWIthImage(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int = R.drawable._140741_3528493,
    greetings: String = "Welcome to ByIt!",
    tagLine: String = "Plan Your Grocery as per Your need"
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_7))
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "App Image" ,
            modifier = Modifier.size(dimensionResource(id = R.dimen.welcome_image_size))
        )
        Text(
            text = greetings,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.welcome_text_color)
        )
        Text(
            text = tagLine,
            fontSize = 12.sp,
            color = colorResource(id = R.color.tagline_text_color)
        )
    }
}

@Composable
fun ConnectButtons(
    modifier: Modifier = Modifier,
    clearAndNavigate: (NavigationRoutes) -> Unit,
    navigate: (NavigationRoutes) -> Unit,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()
){
    val enabled = welcomeViewModel.currentButtonLoading == CurrentButtonLoading.NONE
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Connect With",
            fontSize = 14.sp,
            color = colorResource(id = R.color.tagline_text_color),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_7)),
        )
        Button(
            onClick = {
                navigate(NavigationRoutes.CommonSigning)
            },
            enabled = enabled,
            modifier = Modifier.welcomeScreenButtonModifier(),
            colors = ButtonDefaults.welcomeEmailButtonColors()
        ) {
            Text(
                text = "Email",
                color = colorResource(id = R.color.email_button_text_color),
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_5))
            )
        }

        GoogleButton(
            modifier = Modifier.welcomeScreenButtonModifier(),
            handleResult = { credential ->
                welcomeViewModel.googleAccount(credential,clearAndNavigate)
            },
            buttonText = {
                TextWithLoading(
                    content = {
                        GoogleLogo(
                            text = "oogle",
                            enabled = enabled,
                        )
                    },
                    welcomeViewModel = welcomeViewModel,
                    currentButtonLoading = CurrentButtonLoading.GOOGLE
                )
            }, enabled = enabled
        )

        TextButton(onClick = {
            welcomeViewModel.createAnonymousAccount(clearAndNavigate)
        },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.welcomeTextButtonColors()
        ) {
            TextWithLoading(
                content = {
                    Text(text = stringResource(id = R.string.stay_anonymous))
                },
                welcomeViewModel = welcomeViewModel,
                currentButtonLoading = CurrentButtonLoading.GUEST
            )
        }
    }
}

@Composable
fun TextWithLoading(
    content: @Composable () -> Unit,
    welcomeViewModel: WelcomeViewModel,
    circularProgressIndicatorColor: Color = colorResource(id = R.color.circular_progress_color),
    currentButtonLoading: CurrentButtonLoading,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
        if(welcomeViewModel.currentButtonLoading == currentButtonLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(id = R.dimen.size_20)),
                color = circularProgressIndicatorColor
            )
        }
    }
}