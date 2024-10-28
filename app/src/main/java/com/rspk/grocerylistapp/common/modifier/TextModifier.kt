package com.rspk.grocerylistapp.common.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp

@Composable
fun textStyleForSignUpScreen():TextStyle {
    return TextStyle(
        fontWeight = FontWeight.Bold,
    )
}