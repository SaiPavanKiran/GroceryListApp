package com.rspk.grocerylistapp.common.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.modifier.basicTextFieldContainerModifier
import com.rspk.grocerylistapp.common.modifier.basicTextFieldModifier
import com.rspk.grocerylistapp.common.modifier.customTextFieldColors
import com.rspk.grocerylistapp.common.modifier.customTextFieldModifier
import com.rspk.grocerylistapp.common.modifier.textStyleForSignUpScreen
import kotlinx.coroutines.delay


@Composable
fun CustomTextField(
    value:String,
    onValueChange:(String)-> Unit,
    modifier: Modifier = Modifier,
    singleLine : Boolean = true,
    placeholder: String? = null,
    trailingIcon: Painter? = null,
    leadingIcon: Painter? = null,
    trailingIconClick: () -> Unit = {},
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    visualTransformation: VisualTransformation? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.customTextFieldModifier(),
        singleLine = singleLine,
        textStyle = textStyleForSignUpScreen(),
        shape = RoundedCornerShape(50.dp),
        placeholder = {
            placeholder?.let {
                Text(
                    text = it,
                    modifier = Modifier,
                    style = textStyleForSignUpScreen()
                )
            }
        },
        leadingIcon = {
           leadingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = "Icon",
                )
            }
        },
        trailingIcon = {
            trailingIcon?.let {
                Icon(
                    painter = it,
                    contentDescription = "Icon",
                    modifier = Modifier
                        .clickable {
                            trailingIconClick()
                        },
                    tint = colorResource(id = R.color.text_field_text_color) 
                )
            }
        },
        maxLines = 1,
        keyboardOptions = keyboardOptions ?: KeyboardOptions.Default,
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        colors = TextFieldDefaults.customTextFieldColors()
    )
}

@Composable
fun CustomBasicTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textStyle: TextStyle = TextStyle(
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        fontWeight = FontWeight.SemiBold,
        color = colorResource(id = R.color.top_text_field_text_colors)
    ),
    leadingIcon: Painter = painterResource(id = R.drawable.baseline_search_24),
    trailingIcon: @Composable (Modifier) -> Unit = {},
    placeholder: String? = "Search For An Item"
) {
    var isFocused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        textStyle = textStyle,
        cursorBrush = SolidColor(colorResource(id = R.color.top_text_field_text_colors)),
        modifier = modifier
            .basicTextFieldModifier()
            .onFocusChanged {
                isFocused = it.hasFocus
            },
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .basicTextFieldContainerModifier(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(30.dp),
                        tint = colorResource(id = R.color.top_text_field_text_colors)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    if (value.isEmpty() && !isFocused ) {
                        Text(
                            text = placeholder ?: "",
                            color = colorResource(id = R.color.top_text_field_text_colors),
                            style = textStyle,
                        )
                    } else {
                        innerTextField()
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                trailingIcon(
                    Modifier
                        .size(25.dp) // Fixed size for the trailing icon
                        .clip(RoundedCornerShape(50.dp))
                )
            }
        }
    )
}


@Composable
fun CustomTextFieldWithButtons(
    modifier: Modifier = Modifier,
    quantity: String,
    onQuantityChanged: (String) -> Unit,
    onDeleteClicked: () -> Unit = {},
    decreaseAmountBasedOnItem:Boolean
) {
    val textSize = remember(quantity) { if(quantity.length >= 6) 16.sp else 20.sp }
    val value = if(decreaseAmountBasedOnItem) 0.25f else 1f
    Box(
        modifier = modifier
            .size(height = dimensionResource(id = R.dimen.height_35), width = dimensionResource(id = R.dimen.size_155))
            .wrapContentSize(Alignment.Center)
            .clip(RoundedCornerShape(7.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .size(height = dimensionResource(id = R.dimen.height_35), width = dimensionResource(id = R.dimen.size_155))
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.cart_pill_border_color),
                    shape = RoundedCornerShape(10.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if(quantity.toFloat() != value){
                QuantityButton(
                    icon = "-",
                    onClick = {
                        if (quantity.toFloat() > value)
                            onQuantityChanged((quantity.toFloat() - value).toString())
                    }
                )
            }else{
                QuantityButton(
                    onClick = onDeleteClicked,
                    showDelete = true
                )
            }
            BasicTextField(
                value = quantity,
                onValueChange = {},
                modifier = Modifier
                    .weight(1.35f),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = textSize,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(id = R.color.cart_pill_text_color)
                ),
                interactionSource = null,
                readOnly = true
            )
            QuantityButton(
                icon = "+",
                onClick = {
                    if (quantity.toFloat() < 199)
                    onQuantityChanged((quantity.toFloat() + value).toString())
                }
            )
        }
    }
}

@Composable
fun RowScope.QuantityButton(
    icon: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showDelete: Boolean = false
) {
    Box(
        modifier = modifier
            .weight(1f)
            .clickable { onClick() }
            .fillMaxSize()
            .background(colorResource(id = R.color.cart_pill_background_color)),
        contentAlignment = Alignment.Center
    ) {
        if(showDelete){
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.twotone_delete_outline_24),
                contentDescription = "Delete",
                modifier = Modifier.size(dimensionResource(id = R.dimen.size_25)),
                tint = colorResource(id = R.color.cart_pill_button_icon_color)
            )
        }else{
            Text(
                text = icon,
                color = colorResource(id = R.color.cart_pill_button_icon_color),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun CustomDropDownTextField(
    modifier: Modifier = Modifier,
    value: String,
) {
    BasicTextField(
        value = value,
        onValueChange = {},
        modifier = modifier,
        interactionSource = null,
        readOnly = true,
        textStyle = TextStyle(
            color = colorResource(id = R.color.grocery_text_color),
            textAlign = TextAlign.Center
        )
    )
}