package com.rspk.grocerylistapp.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.modifier.welcomeEmailButtonColors
import com.rspk.grocerylistapp.common.modifier.welcomeTextButtonColors
import com.rspk.grocerylistapp.common.modifier.welcomeScreenButtonModifier

@Composable
fun SmallBox(
    modifier: Modifier = Modifier,
    text:String,
    boxColor: Color,
    textColor: Color,
    highlightBox:Boolean = false,
    onClick:() ->Unit = {}
){
    Box(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.size_32))
            .defaultMinSize(minWidth = dimensionResource(id = R.dimen.size_100))
            .clip(RoundedCornerShape(5.dp))
            .border(
                width = 0.5.dp,
                color = colorResource(id = R.color.grocery_card_border),
                shape = RoundedCornerShape(5.dp)
            )
            .background(boxColor)
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_10)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if(text == "Filters") FontWeight.Bold else if(highlightBox) FontWeight.Bold else FontWeight.Light,
                color = textColor
            )
            if(highlightBox){
                Icon(
                    imageVector = Icons.Default.AddCircle ,
                    contentDescription = "close",
                    modifier = Modifier
                        .rotate(45f)
                        .size(dimensionResource(R.dimen.size_15))
                )
            }
        }
    }
}

@Composable
fun ForgotPassword(
    onClick:() -> Unit
){
    TextButton(onClick = {
        onClick()
    },
        colors = ButtonDefaults.welcomeTextButtonColors(),
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .wrapContentWidth(Alignment.End)
            .padding(bottom = dimensionResource(id = R.dimen.padding_10))
    ) {
        Text(stringResource(id = R.string.forgot_password))
    }
}


@Composable
fun SigningButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    conditionForLoading: Boolean,
    onClick: () -> Unit,
){
    Button(
        onClick = {
            onClick()
        }, enabled = enabled,
        modifier = modifier
            .welcomeScreenButtonModifier(width = 0.75f),
        colors = ButtonDefaults.welcomeEmailButtonColors()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.padding_7)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.continue_text))
            if (conditionForLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_20)),
                    color = colorResource(id = R.color.circular_progress_color)
                )
            }
        }
    }
}