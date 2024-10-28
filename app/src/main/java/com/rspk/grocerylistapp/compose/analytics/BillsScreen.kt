package com.rspk.grocerylistapp.compose.analytics

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.UserUploadedBillsCard
import com.rspk.grocerylistapp.common.modifier.linearProgressModifier
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.compose.home.smallInfoText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@Composable
fun BillsLayout(
    modifier: Modifier = Modifier,
    analyticsViewModel: AnalyticsViewModel,
    userSelectedImage: Uri? = null,
    onImageSelected: (Uri?) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    context: Context = LocalContext.current
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 30.dp)
    ) {
        if(userSelectedImage == null) {
            uploadButton(
                onImageSelected = onImageSelected
            )
            progressIndicator(
                analyticsViewModel = analyticsViewModel
            )
            billItems(
                analyticsViewModel = analyticsViewModel,
                scope = scope,
                context = context
            )
        }else {
            selectedImagePreview(
                analyticsViewModel = analyticsViewModel,
                onImageSelected = onImageSelected,
                userSelectedImage = userSelectedImage,
                scope = scope
            )
        }
    }
}

fun LazyListScope.uploadButton(
    onImageSelected: (Uri) -> Unit
){
    item {
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK){
                it.data?.data?.let { uri ->
                    onImageSelected(uri)
                }
            }
        }
        Box (
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_10))
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_150))
                .clickable {
                    launcher.launch(getIntent())
                },
            contentAlignment = Alignment.Center
        ){
            val stroke = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_150))){
                drawRoundRect(color = Color.LightGray,style = stroke, cornerRadius = CornerRadius(8.dp.toPx()))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_20)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    CustomIconButton(
                        onClick = {
                            launcher.launch(getIntent())
                        },
                        color = IconButtonDefaults.iconButtonColors(
                            containerColor = colorResource(id = R.color.upload_button_background_color),
                            contentColor = colorResource(id = R.color.upload_button_icon_color)
                        ) ,
                        icon = ImageVector.vectorResource(id = R.drawable.outline_file_upload_24)
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_10)))
                    Text(
                        text = "Add Files",
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_5)))
                Text(
                    text = "Users Are Advised to Upload Only Their Bills\nPlease Avoid Uploading Any Sensitive Information",
                    color = colorResource(id = R.color.image_card_small_text_color),
                    style = TextStyle(
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}


fun LazyListScope.progressIndicator(
    analyticsViewModel: AnalyticsViewModel
){
    item {
        BoldTextWithFont25(
            text = "Your Storage Capacity",
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_10))
        )
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_12))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LightTextWithFont12(
                text = "Amount Used: ${formatDecimalString(analyticsViewModel.calculatedSpaceOccupied / (1024.0 * 1024.0))} MB/35 MB"
            )
            LightTextWithFont12(
                text = "${formatDecimalString((analyticsViewModel.calculatedSpaceOccupied/35_000_000.0)*100 )}%",
            )
        }
        LinearProgressIndicator(
            progress =  {(analyticsViewModel.calculatedSpaceOccupied/35_000_000) },
            modifier = Modifier.linearProgressModifier(),
            trackColor = Color.Transparent,
            color = colorResource(id = R.color.progress_bar_color)
        )
    }
}

fun LazyListScope.billItems(
    analyticsViewModel: AnalyticsViewModel,
    scope: CoroutineScope,
    context: Context
) {
    item {
        analyticsViewModel.uploadedImagesList?.let { data ->
            if (data.isNotEmpty()) {
                data.forEach {
                    UserUploadedBillsCard(
                        image = it.uri,
                        fileName = it.fileName,
                        dateUploaded = it.fileName.substringBeforeLast("-"),
                        type = ".${it.type.substringAfter("/")}",
                        fileSize = "${formatDecimalString(it.size / (1024.0 * 1024.0))} MB",
                        menuItems = mapOf(
                            "Download" to {
                                scope.launch {
                                    if (analyticsViewModel.downloadImage(
                                            context,
                                            it.fileName,
                                            it.uri.toString()
                                        )
                                    ) {
                                        SnackBarManager.showMessage("Downloaded Successfully...Please Check Your Downloads")
                                    }
                                }
                            },
                            "Delete" to {
                                scope.launch {
                                    if (analyticsViewModel.deleteImage(it.uri.toString())) {
                                        analyticsViewModel.getUploadedImagesList()
                                        analyticsViewModel.getAmountUsed()
                                        SnackBarManager.showMessage("Deleted Successfully")
                                    }
                                }
                            }
                        )
                    )
                }
            }
        }
    }
}


fun LazyListScope.selectedImagePreview(
    analyticsViewModel: AnalyticsViewModel,
    onImageSelected: (Uri?) -> Unit,
    userSelectedImage: Uri?,
    scope: CoroutineScope
){
    item {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.ready_for_upload),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_10)),
                color = colorResource(id = R.color.upload_preview_heading_text_color)
            )
            Box(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_20))
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(colorResource(id = R.color.upload_preview_frame_color))
            ){
                Image(
                    painter = rememberAsyncImagePainter(model = userSelectedImage),
                    contentDescription = "user Image",
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_10))
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(Color.Black),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_20)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomIconButton(
                    onClick = {
                        onImageSelected(null)
                    },
                    color = IconButtonDefaults.iconButtonColors(
                        containerColor = colorResource(id = R.color.upload_preview_cross_button_background_color),
                        contentColor = colorResource(id = R.color.upload_preview_cross_button_icon_color)
                    ),
                    icon = Icons.Default.Clear,
                    iconModifier = Modifier.size(dimensionResource(id = R.dimen.size_50)),
                    buttonModifier = Modifier.size(dimensionResource(id = R.dimen.size_70))
                )
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.size_60)))
                CustomIconButton(
                    onClick = {
                        scope.launch {
                            val image = userSelectedImage
                            onImageSelected(null)
                            if(analyticsViewModel.uploadImage(image!!)){
                                analyticsViewModel.getUploadedImagesList()
                                analyticsViewModel.getAmountUsed()
                            }
                        }
                    },
                    color = IconButtonDefaults.iconButtonColors(
                        containerColor = colorResource(id = R.color.upload_preview_tick_button_background_color),
                        contentColor = colorResource(id = R.color.upload_preview_tick_button_icon_color)
                    ),
                    icon =Icons.Default.Check,
                    iconModifier = Modifier.size(dimensionResource(id = R.dimen.size_50)),
                    buttonModifier = Modifier.size(dimensionResource(id = R.dimen.size_70))
                )
            }
        }
    }
}

private fun getIntent() =
    Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
    }

private fun formatDecimalString(value: Double):String{
    return String.format("%.2f", value)
}