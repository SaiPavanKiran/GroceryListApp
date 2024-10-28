package com.rspk.grocerylistapp.common.modifier

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.rspk.grocerylistapp.R

@Composable
fun Modifier.welcomeScreenButtonModifier(width:Float = 0.7f):Modifier =
    this
        .fillMaxWidth(width)
        .shadow(
            elevation = 5.dp,
            ambientColor = colorResource(id = R.color.button_shadow_color),
            shape = RoundedCornerShape(30.dp),
            clip = true
        )
        .padding(bottom = dimensionResource(id = R.dimen.padding_7))


@Composable
fun ButtonDefaults.welcomeEmailButtonColors():ButtonColors =
    this.buttonColors(
        containerColor = colorResource(id = R.color.email_button_background),
        contentColor = colorResource(id = R.color.email_button_text_color)
    )

@Composable
fun ButtonDefaults.welcomeTextButtonColors():ButtonColors =
    this.textButtonColors(
        containerColor = Color.Transparent,
        contentColor = colorResource(id = R.color.text_button_text_color)
    )

@Composable
fun ButtonDefaults.transparentButtonColors():ButtonColors =
    this.buttonColors(
        containerColor =  Color.Transparent,
        contentColor = colorResource(id = R.color.transparent_button_content_color)
    )