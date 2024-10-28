package com.rspk.grocerylistapp.compose.analytics

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R

fun LazyGridScope.smallInfoText(
    textModifier: Modifier = Modifier,
    dividerModifier: Modifier = Modifier,
    text:String
){
    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
        Text(
            text = text,
            color = colorResource(id = R.color.analytics_small_info_text_color),
            fontSize = 10.sp,
            modifier = textModifier,
            textAlign = TextAlign.Center,
        )
        HorizontalDivider(
            dividerModifier
                .padding(
                    top = dimensionResource(id = R.dimen.padding_30),
                    bottom = dimensionResource(id = R.dimen.padding_10),
                )
                .padding(horizontal = dimensionResource(id = R.dimen.padding_40)),
            color = colorResource(id = R.color.analytics_small_info_text_color),
        )
    }
}

@Composable
fun TopSubBar(
    composeState: Boolean,
    setComposeState: (Boolean) -> Unit,
    configuration: Configuration = LocalConfiguration.current
){
    if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_55))
                .background(color = colorResource(id = R.color.analytics_sub_nav_background_color))
                .padding(horizontal = dimensionResource(id = R.dimen.padding_10))
        ){
            TopSubBarItem(
                text = stringResource(id = R.string.spendings),
                onClick = {
                    setComposeState(false)
                },
                boxCondition = !composeState
            )
            TopSubBarItem(
                text = stringResource(id = R.string.bills),
                onClick = {
                    setComposeState(true)
                },
                boxCondition = composeState
            )
        }
    }
}

@Composable
fun RowScope.TopSubBarItem(
    text:String,
    onClick: () -> Unit,
    boxCondition: Boolean = false,
){
    Column(
        modifier = Modifier
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                onClick()
            }
            .weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = text,
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_10))
                .weight(0.95f),
            color = colorResource(id = R.color.analytics_sub_nav_text_color)
        )
        if(boxCondition){
            Box(
                modifier = Modifier
                    .weight(0.05f)
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.size_0_5))
                    .background(colorResource(id = R.color.analytics_sub_nav_selected_indicator_color))
            )
        }
    }
}


@Composable
fun CustomIconButton(
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: IconButtonColors,
    icon: ImageVector,
    contentDescription:String = "Button"
){
    IconButton(
        onClick = onClick,
        colors = color,
        modifier = buttonModifier
    ){
        Icon(
            imageVector = icon ,
            contentDescription = contentDescription,
            modifier = iconModifier
        )
    }
}

@Composable
fun BoldTextWithFont25(
    modifier: Modifier=Modifier,
    text:String
){
    Text(
        text = text ,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        color = colorResource(id = R.color.storage_capacity_heading_color)
    )
}

@Composable
fun LightTextWithFont12(text:String){
    Text(
        text = text,
        color = colorResource(id = R.color.amount_used_text_color),
        fontSize = 12.sp,
    )
}