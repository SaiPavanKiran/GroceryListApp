package com.rspk.grocerylistapp.common.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.compose.login.LoginViewModel
import com.rspk.grocerylistapp.navigation.NavigationRoutes

@Composable
fun DeleteAccountAlert(
    loginViewModel: LoginViewModel,
    loginNavigation: (NavigationRoutes) -> Unit,
    configuration: Configuration = LocalConfiguration.current
) {
    AlertDialog(
        title = {
            if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_10)),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .wrapContentSize(unbounded = true)
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.size_400))
                        .background(colorResource(id = R.color.image_background_color))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable._5199971_9082402),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.75f)
                    )
                    Text(
                        text = stringResource(id = R.string.delete_account_alert_text),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(0.25f)
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_20)),
                        color = colorResource(id = R.color.alert_text_color),
                    )
                }
            } else {
                Text(
                    text = stringResource(id = R.string.delete_account_alert_text),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.alert_text_color)
                )
            }
        },
        onDismissRequest = {},
        confirmButton = {
            Button(onClick = {
                loginViewModel.deleteAccount(loginNavigation)
            },
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.alert_yes_button_text_color),
                    containerColor = colorResource(id = R.color.alert_yes_button_background_color)
                )) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(onClick = {
                loginViewModel.reAuthenticateEmail = false
            },
                colors = ButtonDefaults.buttonColors(
                    contentColor = colorResource(id = R.color.alert_no_button_text_color),
                    containerColor = colorResource(id = R.color.alert_no_button_background_color)
                )) {
                Text(text = "No")
            }
        },
        containerColor = colorResource(id = R.color.alert_box_background_color),
    )
}
