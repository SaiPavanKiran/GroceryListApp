package com.rspk.grocerylistapp.common.composables

import android.content.Context
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.logos.GoogleLogo
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.util.Base64
import com.rspk.grocerylistapp.R.string as AppText

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    handleResult:(Credential) -> Unit,
    context: Context = LocalContext.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    buttonText: @Composable () -> Unit = { GoogleLogo("Continue with Google") },
    enabled:Boolean = true,
) {
    Button(
        onClick = {
            val getGoogleIdOption =
                GetGoogleIdOption.Builder()
                    .setServerClientId(context.resources.getString(AppText.google_web_client))
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .setNonce(generateNonce())
                    .build()

            val getCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(getGoogleIdOption)
                .build()

            coroutineScope.launch {
                try {
                    val result = CredentialManager.create(context)
                        .getCredential(
                            request = getCredentialRequest,
                            context = context
                        )
                    handleResult(result.credential)
                }catch (ex:Exception){
                    SnackBarManager.showMessage(ex.message.toString())
                }
            }
        },
        enabled = enabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.google_button_background),
            contentColor = Color.Transparent,
        )
    ){
        buttonText()
    }
}

private fun generateNonce(length: Int = 16): String {
    val secureRandom = SecureRandom()
    val nonceBytes = ByteArray(length)
    secureRandom.nextBytes(nonceBytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes)
}