package com.rspk.grocerylistapp.compose.cartscreen

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomDropDownMenu
import com.rspk.grocerylistapp.common.composables.CustomDropDownTextField
import com.rspk.grocerylistapp.common.composables.UsersCartCard
import com.rspk.grocerylistapp.compose.home.smallInfoText
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.constants.getStringFormattedMonthPlusYear
import com.rspk.grocerylistapp.constants.months_list
import com.rspk.grocerylistapp.model.GroceryItemDetails

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel(),
    configuration: Configuration = LocalConfiguration.current,
    currentMonth: String = getStringFormattedMonthPlusYear(),
    showMenu: Boolean = false
) {
    var showDropDownMenu by rememberSaveable { mutableStateOf(showMenu) }
    var isExpanded by remember { mutableStateOf(false) }
    var textFiledValue by rememberSaveable { mutableStateOf(currentMonth) }
    val portraitConfiguration = configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    LaunchedEffect(key1 = Unit) {
        cartViewModel.getFilteredCartItems(textFiledValue)
        cartViewModel.calculateAvgPrice(textFiledValue.substringAfter(": "))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDropDownMenu = !showDropDownMenu
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        monthCategory(
            showDropDownMenu = showDropDownMenu,
            isExpanded = isExpanded,
            textFiledValue = textFiledValue,
            onExpandedChanged ={
                isExpanded = it
            },
            onTextFieldValueChange = {
                textFiledValue = it
                cartViewModel.getFilteredCartItems(it.substringAfter(": ").trim())
                cartViewModel.calculateAvgPrice(textFiledValue.substringAfter(": "))
            }
        )
        totalAvgPrice(
            cartViewModel = cartViewModel,
            portraitConfiguration = portraitConfiguration
        )
        smallInfoText(
            text1 = "*Above Price is Estimated Average Price\n" +
                    "Original Price Can Vary From The Estimated Price",
            text2 = buildAnnotatedString {
                append("*Long Press to access the monthly cart\n" +
                        "You are current viewing **")
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(textFiledValue)
                pop()
                append("** data")
            },
            textModifier = Modifier
                .padding(bottom = 10.dp)
        )
        cartViewModel.allCartItems?.let { allCartItems ->
            cartScreenCards(
                allCartItems = allCartItems,
                cartViewModel = cartViewModel,
                textFiledValue = textFiledValue
            )
        }
    }
}


fun LazyListScope.monthCategory(
    showDropDownMenu: Boolean,
    isExpanded: Boolean,
    textFiledValue: String,
    onExpandedChanged: (Boolean) -> Unit,
    onTextFieldValueChange: (String) -> Unit
){
    item {
        if (showDropDownMenu) {
            CustomDropDownMenu(
                isExpanded = isExpanded,
                onExpandedChanged = onExpandedChanged,
                textFieldComposable = {
                    CustomDropDownTextField(
                        value = stringResource(id = R.string.chosen_month,textFiledValue),
                        modifier = it
                    )
                },
                onTextFieldValueChange = onTextFieldValueChange,
                items = months_list,
                modifier = Modifier.padding(
                    vertical = dimensionResource(id = R.dimen.padding_20),
                    horizontal = dimensionResource(id = R.dimen.padding_10)
                )
            )
        }
    }
}


fun LazyListScope.totalAvgPrice(
    cartViewModel: CartViewModel,
    portraitConfiguration: Boolean
){
    item {
        val totalPriceFontSize =
            if (portraitConfiguration && cartViewModel.calculatedAvgPrice.length <= 10) 25.sp else
                if (portraitConfiguration && cartViewModel.calculatedAvgPrice.length >= 17) 18.sp else 23.sp
        Text(
            text = buildAnnotatedString {
                append("Total Avg Price: ")
                pushStyle(
                    SpanStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = totalPriceFontSize
                    )
                )
                append(cartViewModel.calculatedAvgPrice)
                pop()
            },
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 20.dp),
            textAlign = TextAlign.Start
        )
    }
}

fun LazyListScope.cartScreenCards(
    allCartItems: List<GroceryItemDetails>,
    cartViewModel: CartViewModel,
    textFiledValue: String
){
    items(allCartItems, key = { it.id }) {
        var isChecked by rememberSaveable { mutableStateOf(it.isChecked) }
        var quantity by rememberSaveable { mutableStateOf("0") }
        UsersCartCard(
            image = it.image,
            title = it.id.substringBefore("("),
            description = it.description,
            avgPrice = it.avgPrice,
            rating = it.rating,
            isChecked = isChecked,
            quantityType = it.quantityType,
            onCheckedChange = { value ->
                isChecked = value
                cartViewModel.updateCartStatus(
                    it.copy(
                        isChecked = value
                    )
                )
                cartViewModel.calculateAvgPrice(textFiledValue.substringAfter(": "))
            },
            quantity = if (it.quantity.toString() != quantity && quantity != "0") quantity else formatDecimal(
                it.quantity.toString()
            ),
            onQuantityChanged = {
                quantity = formatDecimal(it)
            },
            decreaseAmountBasedOnItem = it.quantityType == "kg" || it.quantityType == "ltr",
            isItemNeedToUpdate = if (quantity != "0") formatDecimal(it.quantity.toString()) != quantity else false,
            onUpdateClick = {
                cartViewModel.updateCartStatus(
                    it.copy(
                        quantity = quantity.toFloat()
                    )
                )
                cartViewModel.calculateAvgPrice(textFiledValue.substringAfter(": "))
            },
            onDeleteClick = {
                quantity = "0"
                cartViewModel.deleteItem(it.id)
                cartViewModel.calculateAvgPrice(textFiledValue.substringAfter(": "))
            }
        )
    }
}