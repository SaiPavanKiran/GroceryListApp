package com.rspk.grocerylistapp.common.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.rspk.grocerylistapp.R

@Composable
fun Modifier.scaffoldTopBarModifier(): Modifier {
    return this
        .statusBarsPadding()
        .clip(RoundedCornerShape(topStart = 17.dp, topEnd = 17.dp))
        .background(
            Brush.horizontalGradient(
                colors = listOf(
                    colorResource(id = R.color.top_bar_color_1),
                    colorResource(id = R.color.top_bar_color_2)
                )
            )
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDefaults.customTopAppBarColors() =
   this.topAppBarColors(
        containerColor = Color.Transparent
    )