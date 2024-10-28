package com.rspk.grocerylistapp.compose.login

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomTextField
import com.rspk.grocerylistapp.common.composables.DeleteAccountAlert
import com.rspk.grocerylistapp.common.composables.ForgotPassword
import com.rspk.grocerylistapp.common.composables.GoogleButton
import com.rspk.grocerylistapp.common.composables.SigningButton
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.navigation.NavigationRoutes

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginNavigation: (NavigationRoutes) -> Unit,
    activateDeleteAccount: Boolean = false,
    inputEmail: String = "",
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf(inputEmail) }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    if (loginViewModel.reAuthenticateEmail) {
        DeleteAccountAlert(
            loginNavigation = loginNavigation,
            loginViewModel = loginViewModel
        )
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            LoginContent(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                isPasswordVisible = isPasswordVisible,
                isPasswordVisibleChanged = { isPasswordVisible = !isPasswordVisible },
                loginViewModel = loginViewModel,
                loginNavigation = loginNavigation,
                activateDeleteAccount = activateDeleteAccount
            )
        }
    }
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    isPasswordVisibleChanged: () -> Unit,
    loginViewModel: LoginViewModel,
    loginNavigation: (NavigationRoutes) -> Unit,
    activateDeleteAccount: Boolean
) {

    if (loginViewModel.doesForgotEmailSent) {
        SnackBarManager.showMessage(stringResource(id = R.string.email_sent))
    }
    CustomTextField(
        value = email,
        onValueChange = onEmailChange,
        placeholder = stringResource(id = R.string.email),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_10))
    )

    CustomTextField(
        value = password,
        onValueChange = onPasswordChange,
        placeholder = stringResource(id = R.string.password),
        trailingIcon = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24) else painterResource(
            id = R.drawable.baseline_visibility_off_24
        ),
        trailingIconClick = isPasswordVisibleChanged,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(
            '\u2022'
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )

    ForgotPassword {
        if (email.isNotEmpty()) {
            loginViewModel.forgotPassword(email)
        }else{
            SnackBarManager.showMessage("Please Enter Your Email Before Clicking On Forgot Password")
        }
    }

    SigningButton(
        enabled = loginViewModel.currentButtonLoading == CurrentButtonLoading.NONE,
        conditionForLoading = loginViewModel.currentButtonLoading == CurrentButtonLoading.LOGIN
    ) {
        if (activateDeleteAccount) {
            loginViewModel.reAuthenticateUser(email, password)
        } else {
            loginViewModel.login( email, password, loginNavigation)
        }
    }
}