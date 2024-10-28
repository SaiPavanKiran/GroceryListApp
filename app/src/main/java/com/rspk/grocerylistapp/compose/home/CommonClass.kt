package com.rspk.grocerylistapp.compose.home

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomDropDownTextField
import com.rspk.grocerylistapp.common.composables.SearchResultsItemDetailCard
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.constants.getStringFormattedMonth
import com.rspk.grocerylistapp.model.GroceryItemDetails
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun LazyListScope.groceryAndSearchCommonCard(
    it: List<GroceryItemDetails>,
    homeViewModel: HomeViewModel,
    tagColor: Color = Color.Transparent,
) {
    items(it, key = { it.id }) {
        var isExpanded by remember { mutableStateOf(false) }
        var value by remember { mutableStateOf("1") }
        SearchResultsItemDetailCard(
            image = it.image,
            text = it.id,
            rating = it.rating,
            isExpanded = isExpanded,
            onExpandedChanged = { isExpanded = it },
            onTextFieldValueChanged = {
                value = formatDecimal(it)
                isExpanded = !isExpanded
            },
            quantityType = it.quantityType,
            avgPrice = it.avgPrice,
            textFiledComposable = { modifier ->
                CustomDropDownTextField(
                    value = "Quantity: $value",
                    modifier = modifier
                )
            },
            onClick = {
                homeViewModel.addMonthlyGroceryList(
                    GroceryItemDetails(
                        id = it.id + "(${getStringFormattedMonth()})",
                        description = it.description,
                        image = it.image,
                        rating = it.rating,
                        quantityType = it.quantityType,
                        avgPrice = it.avgPrice,
                        type = it.type,
                        subType = it.subType,
                        quantity = value.toFloatOrNull() ?: 0f,
                    )
                )
            },
            isAlreadyPresentInCart = homeViewModel.userCartDetails?.contains(it.id + "(${getStringFormattedMonth()})") == true,
            dropdownMenuBoxItemsScope = it.quantityType == "kg" || it.quantityType == "ltr",
            tagColor = tagColor
        )
    }
}


fun LazyListScope.smallInfoText(
    textModifier: Modifier = Modifier,
    dividerModifier: Modifier = Modifier,
    text:String
){
    item {
        Text(
            text = text,
            color = colorResource(id = R.color.small_info_text_color),
            fontSize = 10.sp,
            modifier = textModifier,
            textAlign = TextAlign.Center
        )
        HorizontalDivider(
            dividerModifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_10))
                .size(
                    width = dimensionResource(id = R.dimen.size_250),
                    height = dimensionResource(id = R.dimen.size_0_2)
                ),
            color = colorResource(id = R.color.small_info_text_color),
        )
    }
}

fun LazyListScope.smallInfoText(
    textModifier: Modifier = Modifier,
    dividerModifier: Modifier = Modifier,
    text1: String,
    text2:AnnotatedString
){
    item {
        Text(
            text = text1,
            color = colorResource(id = R.color.small_info_text_color),
            fontSize = 12.sp,
            modifier = textModifier,
            style = TextStyle(textAlign = TextAlign.Center)
        )
        HorizontalDivider(
            dividerModifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_10))
                .size(
                    width = dimensionResource(id = R.dimen.size_250),
                    height = dimensionResource(id = R.dimen.size_0_2)
                ),
            color = colorResource(id = R.color.small_info_text_color),
        )
        Text(
            text = text2,
            color = colorResource(id = R.color.small_info_text_color),
            fontSize = 12.sp,
            modifier = textModifier,
            style = TextStyle(textAlign = TextAlign.Center)
        )
    }
}