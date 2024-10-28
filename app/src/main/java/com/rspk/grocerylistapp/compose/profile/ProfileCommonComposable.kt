package com.rspk.grocerylistapp.compose.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.R

@Composable
fun ProfileColumnLayout(
    modifier: Modifier = Modifier,
    dividerModifier: Modifier = Modifier,
    showDivider: Boolean = true,
    content: @Composable () -> Unit
){
    Column(
        modifier = Modifier
            .padding(
                top = dimensionResource(id = R.dimen.padding_15),
                bottom = dimensionResource(id = R.dimen.padding_20))
            .padding(
                start = dimensionResource(id = R.dimen.padding_30),
                end = dimensionResource(id = R.dimen.padding_15)
            )
    ) {
        content()
    }
    if(showDivider){
        HorizontalDivider(
            modifier = dividerModifier
                .fillMaxWidth()
                .height(0.2.dp),
            color = Color.LightGray
        )
    }
}

@Composable
fun ProfileRowLayout(
    content1:@Composable (Modifier) -> Unit,
    content2:@Composable (Modifier) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_90)),
        verticalAlignment = Alignment.CenterVertically
    ){
        content1(
            Modifier
                .weight(1f)
        )
        VerticalDivider(
            modifier = Modifier
                .fillMaxHeight()
                .width(0.2.dp),
            color = Color.LightGray
        )
        content2(
            Modifier
                .weight(1f)
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.2.dp),
        color = Color.LightGray
    )
}

@Composable
fun ProfileDetail(
    modifier: Modifier = Modifier,
    value:String,
    onValueChange:(String) -> Unit,
    bottomPadding: Dp = 15.dp,
    topPadding: Dp = 0.dp,
    image: ImageVector? = null,
    textSize: TextUnit = 12.sp,
    textAlign: TextAlign = TextAlign.Start,
    imageSize: Dp = 25.dp,
    fontWeight: FontWeight? = null,
    contentDescription:String? = null,
    color: Color = Color.LightGray,
    readOnly: Boolean = true,
    showIcon: Boolean = true,
    keyboardOptions: KeyboardOptions? =null,
){
    Row(
        modifier = modifier
            .padding(top = topPadding, bottom = bottomPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if(showIcon && image != null){
            Icon(
                imageVector = image,
                contentDescription = contentDescription,
                tint = color,
                modifier = Modifier
                    .size(imageSize)
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = textSize,
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.inverseSurface),
            readOnly = readOnly,
            keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
            modifier = Modifier
        )
    }
}

@Composable
fun ProfileDetailWithClickable(
    modifier: Modifier = Modifier,
    value:String,
    bottomPadding: Dp = 15.dp,
    topPadding: Dp = 0.dp,
    image: ImageVector? = null,
    textSize: TextUnit = 12.sp,
    textAlign: TextAlign = TextAlign.Start,
    imageSize: Dp = 25.dp,
    fontWeight: FontWeight? = null,
    contentDescription:String? = null,
    color: Color = Color.LightGray,
    showIcon: Boolean = true,
    onClick: () -> Unit
){
    Row(
        modifier = modifier
            .padding(top = topPadding, bottom = bottomPadding)
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if(showIcon && image != null){
            Icon(
                imageVector = image,
                contentDescription = contentDescription,
                tint = color,
                modifier = Modifier
                    .size(imageSize)
            )
        }
        Text(
            text = value,
            style = TextStyle(
                fontSize = textSize,
                color = color,
                fontWeight = fontWeight,
                textAlign = textAlign
            )
        )
    }
}


@Composable
fun ProfileImageWithUserName(
    modifier: Modifier = Modifier,
    image:String?,
    name:String,
    onNameChange:(String) -> Unit,
    userBio:String,
    onBioChange:(String) -> Unit,
    readOnly: Boolean = true,
    onClick:()  -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = dimensionResource(id = R.dimen.padding_20),
                bottom = dimensionResource(id = R.dimen.padding_25)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(23.dp)
    ) {
        Box(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.size_90))
                .clip(CircleShape)
                .border(
                    width = 0.2.dp,
                    color = colorResource(id = R.color.profile_image_border_color),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    if (!readOnly) {
                        onClick()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(image),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .fillMaxSize()
                    .size(dimensionResource(id = R.dimen.size_80))
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        PairOfTwoText(
            value1 = name,
            onValue1Change = onNameChange,
            value2 = userBio,
            onValue2Change = onBioChange,
            readOnly = readOnly
        )
    }
}

@Composable
fun PairOfTwoText(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    arrangement: Arrangement.Vertical = Arrangement.Top,
    value1:String,
    onValue1Change:(String) -> Unit = {},
    text1Size: TextUnit = 25.sp,
    value2:String,
    onValue2Change:(String) -> Unit = {},
    text2Size: TextUnit = 12.sp,
    readOnly: Boolean = true,
    textAlign: TextAlign = TextAlign.Start
){
    Column(
        modifier = modifier,
        horizontalAlignment = alignment,
        verticalArrangement = arrangement
    ) {
        ProfileDetail(
            value = value1,
            onValueChange = onValue1Change,
            textSize = text1Size,
            fontWeight = FontWeight.Bold,
            showIcon = false,
            bottomPadding = 7.dp,
            readOnly = readOnly,
            color = colorResource(id = R.color.profile_name_text_color),
            textAlign = textAlign
        )
        ProfileDetail(
            value = value2,
            onValueChange = onValue2Change,
            textSize = text2Size,
            showIcon = false,
            color = colorResource(id = R.color.profile_bio_text_color),
            bottomPadding = 0.dp,
            readOnly = readOnly,
            textAlign = textAlign
        )
    }
}

@Composable
fun getUri(
    modifier: Modifier = Modifier,
    onImageChange: (String) -> Unit,
) =
    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            it.data?.data?.let {
                onImageChange(it.toString())
            }
        }
    }

internal fun getIntent() =
    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
    }
