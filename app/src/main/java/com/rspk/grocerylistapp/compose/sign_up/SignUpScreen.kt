package com.rspk.grocerylistapp.compose.sign_up

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
import com.rspk.grocerylistapp.common.composables.GoogleButton
import com.rspk.grocerylistapp.common.composables.SigningButton
import com.rspk.grocerylistapp.common.logos.GoogleLogo
import com.rspk.grocerylistapp.common.modifier.welcomeScreenButtonModifier
import com.rspk.grocerylistapp.compose.login.LoginContent
import com.rspk.grocerylistapp.compose.welcome.WelcomeViewModel
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.navigation.NavigationRoutes

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    signUpNavigation: (NavigationRoutes) -> Unit,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    continueWithEmail: Boolean = false
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var reEnterPassword by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        SignUpScreenContent(
            name = name,
            email = email,
            password = password,
            reEnterPassword = reEnterPassword,
            onNameChange = { name = it },
            onEmailChange = { email = it },
            onPasswordChange = { password = it },
            onReEnterPasswordChange = { reEnterPassword = it },
            isPasswordVisible = isPasswordVisible,
            isPasswordVisibleChanged = { isPasswordVisible = !isPasswordVisible },
            signUpViewModel = signUpViewModel,
            signUpNavigation = signUpNavigation,
            continueWithEmail = continueWithEmail
        )
    }
}

@Composable
fun SignUpScreenContent(
    name: String,
    email: String,
    password: String,
    reEnterPassword: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onReEnterPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    isPasswordVisibleChanged: () -> Unit,
    signUpViewModel: SignUpViewModel,
    signUpNavigation: (NavigationRoutes) -> Unit,
    continueWithEmail: Boolean
) {
    val enabled = signUpViewModel.currentButtonLoading == CurrentButtonLoading.NONE
    CustomTextField(
        value = name,
        onValueChange = onNameChange,
        placeholder = stringResource(id = R.string.name),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_10))
    )

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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_10))
    )

    CustomTextField(
        value = reEnterPassword,
        onValueChange = onReEnterPasswordChange,
        placeholder = stringResource(id = R.string.password),
        visualTransformation = PasswordVisualTransformation('\u2022'),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_20))
    )


    SigningButton(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_10)),
        enabled = enabled,
        conditionForLoading = signUpViewModel.currentButtonLoading == CurrentButtonLoading.SIGN_IN
    ) {
        if (signUpViewModel.conditionsMetCheck(name, email, password, reEnterPassword)) {
            if (continueWithEmail) {
                signUpViewModel.continueWithEmail(name, email, password, signUpNavigation)
            } else {
                signUpViewModel.signUp(name, email, password, signUpNavigation)
            }
        }
    }


    if (continueWithEmail) {
        GoogleButton(
            modifier = Modifier.welcomeScreenButtonModifier(width = 0.75f),
            handleResult = { credential ->
                signUpViewModel.continueWithGoogle(credential,signUpNavigation)
            },
            buttonText = {
                TextWithLoading(
                    content = {
                        GoogleLogo(
                            text = "oogle",
                            enabled = enabled,
                        )
                    },
                    signUpViewModel = signUpViewModel,
                    currentButtonLoading = CurrentButtonLoading.GOOGLE
                )
            }, enabled = enabled
        )
    }
}

@Composable
fun TextWithLoading(
    content: @Composable () -> Unit,
    signUpViewModel: SignUpViewModel,
    circularProgressIndicatorColor: Color = colorResource(id = R.color.circular_progress_color),
    currentButtonLoading: CurrentButtonLoading,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
        if(signUpViewModel.currentButtonLoading == currentButtonLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(dimensionResource(id = R.dimen.size_20)),
                color = circularProgressIndicatorColor
            )
        }
    }
}