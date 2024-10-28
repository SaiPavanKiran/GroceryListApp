package com.rspk.grocerylistapp.common.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CustomTransparentCard(
    modifier: Modifier = Modifier,
    customWidth: Dp = 75.dp,
    customHeight: Dp = 120.dp,
    @DrawableRes image: Int,
    text: String,
    onClick:()->Unit ={},
){
    Column(
        modifier = modifier
            .size(width = customWidth, height = customHeight)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.size_45))
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.home_category_card_box_color))
                    .border(
                        width = dimensionResource(id = R.dimen.border_0_7),
                        color = colorResource(id = R.color.home_category_ring_color),
                        shape = CircleShape
                    )
            )
            Image(
                painter = rememberAsyncImagePainter(model = image) ,
                contentDescription = text,
                modifier = Modifier.size(dimensionResource(id = R.dimen.size_50))
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_7)))
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_10))
                .wrapContentSize(unbounded = true),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.home_category_text_color),
            style= TextStyle(fontWeight = FontWeight.W500)
        )
    }
}

@Composable
fun CustomUserAnalyticsCard(
    modifier: Modifier = Modifier,
    customWidth: Dp = 75.dp,
    customHeight: Dp = 120.dp,
    @DrawableRes image: Int,
    text: String,
    onClick:()->Unit ={},
){
    Column(
        modifier = modifier
            .size(width = customWidth, height = customHeight)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_5))
            .clip(RoundedCornerShape(4.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.shortcuts_row_card_color))
                .padding(dimensionResource(id = R.dimen.padding_5))
                .weight(0.35f),
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            color = colorResource(id = R.color.shortcuts_row_text_color),
            style= TextStyle(fontWeight = FontWeight.W500)
        )

        Image(
            painter = rememberAsyncImagePainter(model = image) ,
            contentDescription = text,
            modifier = Modifier
                .fillMaxSize()
                .weight(1.15f)
                .background(colorResource(id = R.color.shortcuts_row_card_color))
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}