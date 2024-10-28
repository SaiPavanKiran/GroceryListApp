package com.rspk.grocerylistapp.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDownMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    textFieldComposable: @Composable (Modifier) -> Unit,
    onTextFieldValueChange: (String) -> Unit,
    items: List<String>
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChanged,
        modifier = modifier
    ) {
        AnchoredTextField(
            textFieldComposable = textFieldComposable
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChanged(false) },
            modifier = Modifier
                .background(colorResource(id = R.color.dropdown_menu_background_color))
                .wrapContentSize(unbounded = true)
                .fillMaxWidth(),
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = it,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.dropdown_menu_text_color)
                        ) },
                    onClick = {
                        onTextFieldValueChange(it)
                        onExpandedChanged(false)
                    },
                    modifier = Modifier
                        .padding(start = dimensionResource(id = R.dimen.padding_20))
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxScope.AnchoredTextField(
    textFieldComposable:@Composable (Modifier) -> Unit,
){
    var isFocused by remember { mutableStateOf(false) }
    textFieldComposable(
        Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.size_50))
            .then(
                if (isFocused) {
                    Modifier.border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(10.dp)
                    )
                } else {
                    Modifier.border(width = 0.dp, color = Color.Transparent)
                }
            )
            .onFocusEvent {
                isFocused = it.isFocused
            }
            .padding(dimensionResource(id = R.dimen.padding_5))
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(7.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.drop_down_menu_text_field_background))
            .menuAnchor(
                type = MenuAnchorType.PrimaryEditable,
                enabled = true
            )
            .wrapContentSize(Alignment.Center)
    )
}


@Composable
fun CustomOptionMenu(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    items: Map<String,() -> Unit>,
){
    Box{
        IconButton(
            onClick = {onExpandedChanged(!isExpanded)},
            modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = colorResource(id = R.color.menu_option_icon_color)
            )
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { onExpandedChanged(false) },
            containerColor = colorResource(id = R.color.menu_options_box_color)
        ) {
            items.forEach {
                DropdownMenuItem(
                    text ={
                        Text(text = it.key) },
                    onClick = {
                        onExpandedChanged(false)
                        it.value()
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = colorResource(id = R.color.menu_option_text_color),
                    )
                )
            }
        }
    }
}