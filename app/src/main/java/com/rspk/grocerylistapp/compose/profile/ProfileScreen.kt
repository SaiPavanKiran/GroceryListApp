package com.rspk.grocerylistapp.compose.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import java.util.Locale

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    clearAndNavigate: (NavigationRoutes) -> Unit = {},
    navigate: (NavigationRoutes) -> Unit = {}
) {
    profileViewModel.currentUserData?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val guestImage = stringResource(id = R.string.guest_image_url)
            UserRelatedDetails(
                profileViewModel = profileViewModel,
                inputName = it.userName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }.ifEmpty { "No Name Available" },
                inputUserBio = it.userBio.ifEmpty { "No Bio Available" },
                inputPhoneNumber = it.phoneNumber.ifEmpty { "No Phone Number Available" },
                inputEmail = it.email.ifEmpty { "No Email Available" },
                inputImage = it.photoUrl?.ifEmpty { guestImage }
            )
            AccountCreationDetails(
                inputAccountActiveSince = it.accountCreationDate,
                inputLastLoginDate = it.lastLoginDate
            )
            AccountRelatedDetails(
                inputIsAdmin = "Admin Access: ${if(it.admin) "Enabled" else "Restricted"}",
                inputIsGuest = "Guest Account: ${if(it.guestAccount) "Yes" else "No"}",
                inputFirebaseInstallationId = "Installation Id: ${it.firebaseInstallationId}",
            )
            AccountSignOutDetails(
                profileViewModel = profileViewModel,
                secondText = if(it.guestAccount) "Continue With email" else "Delete Account",
                secondTextImage = if(it.guestAccount) ImageVector.vectorResource(id = R.drawable.baseline_alternate_email_24) else Icons.Outlined.Delete,
                secondTextColor =  if(it.guestAccount) MaterialTheme.colorScheme.inverseSurface else colorResource(id = R.color.profile_logout_text_color),
                onLogOut = {
                    profileViewModel.signOut(clearAndNavigate)
                },
                onClick2 = {
                    if(it.guestAccount){
                        navigate(NavigationRoutes.SignUpScreen(true))
                    }else{
                        navigate(NavigationRoutes.LoginScreen(accountDeletion = true, email = it.email))
                    }
                }
            )
        }
    }
}