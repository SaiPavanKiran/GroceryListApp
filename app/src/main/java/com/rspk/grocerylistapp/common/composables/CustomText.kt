package com.rspk.grocerylistapp.common.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.model.AppProposedList

@Composable
fun ColumnScope.GroceryCardCustomText(
    modifier: Modifier = Modifier,
    text:String,
    fontSize:TextUnit = 16.sp,
    fontWeight:FontWeight? = null
){
    Text(
        text = text,
        modifier = modifier
            .weight(0.1f)
            .padding(dimensionResource(id = R.dimen.padding_5)),
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = colorResource(id = R.color.grocery_text_color)
    )
}

@Composable
fun AppProposedListTitle(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 30.sp,
    fontWeight: FontWeight = FontWeight.Bold
){
    Text(
        text = text,
        color = Color.LightGray,
        fontSize = fontSize,
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_5)),
        textAlign = TextAlign.Center,
        fontWeight = fontWeight
    )
}