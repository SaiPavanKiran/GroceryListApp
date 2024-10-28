package com.rspk.grocerylistapp.common.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.rspk.grocerylistapp.R

@Composable
fun Modifier.filterRowModifier():Modifier {
     return this
        .fillMaxWidth()
        .defaultMinSize(minHeight = 50.dp)
        .background(colorResource(id = R.color.light_card_color))
        .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 13.dp))
}
