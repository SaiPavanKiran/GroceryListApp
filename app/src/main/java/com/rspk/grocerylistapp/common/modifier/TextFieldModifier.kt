package com.rspk.grocerylistapp.common.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.rspk.grocerylistapp.R

@Composable
fun TextFieldDefaults.customTextFieldColors():TextFieldColors{
    return this.colors(
        unfocusedContainerColor = colorResource(id = R.color.text_field_background_color),
        focusedContainerColor = colorResource(id = R.color.text_field_background_color),
        unfocusedTextColor = colorResource(id = R.color.text_field_text_color),
        focusedTextColor = colorResource(id = R.color.text_field_text_color),
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedPlaceholderColor = colorResource(id = R.color.text_field_placeholder_color),
        focusedPlaceholderColor = colorResource(id = R.color.text_field_placeholder_color),
        cursorColor = colorResource(id = R.color.text_field_text_color)
    )
}

@Composable
fun Modifier.customTextFieldModifier():Modifier {
    return this
        .height(dimensionResource(id = R.dimen.size_60))
        .fillMaxWidth(0.75f)
        .shadow(
            elevation = 10.dp,
            ambientColor = Color.Black.copy(0.2f),
            shape = RoundedCornerShape(30.dp),
            clip = true
        )
}

@Composable
fun Modifier.basicTextFieldModifier():Modifier{
    return this
        .fillMaxWidth(0.95f)
        .height(dimensionResource(id = R.dimen.size_47))

}

@Composable
fun Modifier.basicTextFieldContainerModifier():Modifier{
    return this
        .fillMaxSize()
        .height(dimensionResource(id = R.dimen.size_47))
        .clip(RoundedCornerShape(7.dp))
        .background(colorResource(id = R.color.top_text_field_container))
        .border(
            width = 0.3.dp,
            color = colorResource(id = R.color.cart_buttons_color),
            shape = RoundedCornerShape(7.dp)
        )
        .padding(horizontal = dimensionResource(id = R.dimen.padding_10))
}