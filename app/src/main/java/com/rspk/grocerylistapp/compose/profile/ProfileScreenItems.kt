package com.rspk.grocerylistapp.compose.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import com.rspk.grocerylistapp.model.UserInfo
import com.rspk.grocerylistapp.model.UserInfoUpdate
import java.util.Locale

@Composable
fun UserRelatedDetails(
    profileViewModel: ProfileViewModel,
    inputName: String,
    inputUserBio: String,
    inputPhoneNumber: String,
    inputEmail: String,
    inputImage: String?
){
    var readOnly by rememberSaveable { mutableStateOf(true) }
    var name by rememberSaveable { mutableStateOf(inputName) }
    var userBio by rememberSaveable { mutableStateOf(inputUserBio) }
    var phoneNumber by rememberSaveable { mutableStateOf(inputPhoneNumber) }
    var email by rememberSaveable { mutableStateOf(inputEmail) }
    var image by rememberSaveable { mutableStateOf(inputImage) }
    val launcher = getUri(onImageChange = {image = it})
    ProfileColumnLayout {
        IconButton(onClick = {
            readOnly = !readOnly
            if(readOnly){
                profileViewModel.uploadUserData(
                    UserInfoUpdate(
                        userName = name,
                        userBio = userBio,
                        phoneNumber = phoneNumber,
                        email = email,
                        photoUrl = image,
                    ),
                    needToStoreUserImage = image != inputImage
                )
            }
         },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        ) {
            if(profileViewModel.currentButtonLoading == CurrentButtonLoading.USER_PROFILE_UPDATE) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.profile_pencil_icon_color)
                )
            }else{
                Icon(
                    imageVector = if (readOnly) Icons.Default.Edit else Icons.Default.CheckCircle,
                    contentDescription = "Edit",
                    tint = colorResource(id = R.color.profile_pencil_icon_color)
                )
            }
        }
        ProfileImageWithUserName(
            image = image,
            name = name,
            onNameChange = { name = it },
            userBio = userBio,
            onBioChange = { userBio = it },
            readOnly = readOnly,
            onClick = {
                launcher.launch(getIntent())
            }
        )
        ProfileDetail(
            value = phoneNumber ,
            onValueChange = { phoneNumber = it },
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            image = Icons.Default.Call,
            color = colorResource(id = R.color.profile_contact_text_color)
        )
        ProfileDetail(
            value = email,
            onValueChange = { email = it },
            readOnly = readOnly,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            image = ImageVector.vectorResource(id = R.drawable.outline_email_24),
            color = colorResource(id = R.color.profile_contact_text_color)
        )
    }
}


@Composable
fun AccountCreationDetails(
    inputAccountActiveSince: String,
    inputLastLoginDate:String
){
    ProfileRowLayout(
        content1 = {
            PairOfTwoText(
                modifier = it,
                value1 = "Created On" ,
                value2 = inputAccountActiveSince,
                textAlign = TextAlign.Center,
                alignment = Alignment.CenterHorizontally,
                arrangement = Arrangement.Center,
                text1Size = 18.sp,
                text2Size = 13.sp
            )
        },
        content2 = {
            PairOfTwoText(
                modifier = it,
                value1 = "Last Login On" ,
                value2 = inputLastLoginDate,
                textAlign = TextAlign.Center,
                alignment = Alignment.CenterHorizontally,
                arrangement = Arrangement.Center,
                text1Size = 18.sp,
                text2Size = 13.sp
            )
        }
    )
}

@Composable
fun AccountRelatedDetails(
    inputIsAdmin: String,
    inputIsGuest: String,
    inputFirebaseInstallationId: String,
){
    ProfileColumnLayout {
        ProfileDetail(
            value = inputIsAdmin ,
            onValueChange = { },
            image = Icons.Outlined.Face,
            imageSize = 26.dp,
            textSize = 14.sp,
            bottomPadding = 20.dp,
            topPadding = 13.dp,
            color = colorResource(id = R.color.profile_account_details_text_color)
        )
        ProfileDetail(
            value = inputIsGuest,
            onValueChange = { },
            image = Icons.Outlined.Person,
            imageSize = 26.dp,
            textSize = 14.sp,
            bottomPadding = 20.dp,
            color = colorResource(id = R.color.profile_account_details_text_color)
        )
        ProfileDetail(
            value = inputFirebaseInstallationId,
            onValueChange = { },
            image = ImageVector.vectorResource(id = R.drawable.outline_local_fire_department_24),
            imageSize = 26.dp,
            textSize = 14.sp,
            bottomPadding = 20.dp,
            color = colorResource(id = R.color.profile_account_details_text_color)
        )
    }
}

@Composable
fun AccountSignOutDetails(
    profileViewModel: ProfileViewModel,
    logOut:String = "Log Out",
    secondText:String ,
    secondTextImage:ImageVector,
    secondTextColor: Color,
    onLogOut: () -> Unit,
    onClick2: () -> Unit
){
    ProfileColumnLayout(
        showDivider = false
    ) {
        Row{
            ProfileDetailWithClickable(
                value = logOut,
                image = ImageVector.vectorResource(id = R.drawable.baseline_logout_24),
                imageSize = 26.dp,
                textSize = 14.sp,
                bottomPadding = 20.dp,
                topPadding = 13.dp,
                color = colorResource(id = R.color.profile_logout_text_color),
                onClick = {
                    onLogOut()
                }
            )
            if(profileViewModel.currentButtonLoading == CurrentButtonLoading.LOGOUT){
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = colorResource(id = R.color.circular_progress_color)
                )
            }
        }
        ProfileDetailWithClickable(
            value = secondText,
            image = secondTextImage ,
            color = secondTextColor,
            imageSize = 26.dp,
            textSize = 14.sp,
            bottomPadding = 20.dp,
            onClick = {
                if(profileViewModel.currentButtonLoading == CurrentButtonLoading.NONE) {
                    onClick2()
                }
            }
        )
    }
}