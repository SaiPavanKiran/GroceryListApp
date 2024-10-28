package com.rspk.grocerylistapp.compose.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomDropDownTextField
import com.rspk.grocerylistapp.common.composables.SearchResultsItemDetailCard
import com.rspk.grocerylistapp.common.composables.SmallBox
import com.rspk.grocerylistapp.common.modifier.filterRowModifier
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.model.GroceryItemDetails
import java.util.SortedSet
import java.util.logging.Filter

@SuppressLint("MutableCollectionMutableState")
@Composable
fun GroceryList(
    filteredList:List<GroceryItemDetails>?,
    currentSearchList:List<String>,
    onCurrentItemsFilteringStringChange:(String) -> Unit,
    onSubListChanged:(List<String>) -> Unit = {} ,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    var userSelectedOptions by rememberSaveable { mutableStateOf(listOf<String>()) }
    var list by rememberSaveable { mutableStateOf(currentSearchList.toSortedSet()) }

    BackHandler {
        onCurrentItemsFilteringStringChange("")
    }

    LazyRow(
        modifier = Modifier.filterRowModifier(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_5))
    ) {
        filtersRow(
            userSelectedOptions = userSelectedOptions,
            list = list,
            selectedItemOnClick = {
                userSelectedOptions = userSelectedOptions.minus(it)
                list = list.plus(it).toSortedSet()
                onSubListChanged(userSelectedOptions)
            },
            unSelectedItemOnClick = {
                userSelectedOptions = userSelectedOptions.plus(list.elementAt(it))
                list = list.minus(list.elementAt(it)).toSortedSet()
                onSubListChanged(userSelectedOptions)
            }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_30)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        groceryList(
            filteredList = filteredList,
            homeViewModel = homeViewModel
        )
    }
}


@SuppressLint("MutableCollectionMutableState")
fun LazyListScope.filtersRow(
    userSelectedOptions:List<String>,
    list:SortedSet<String>,
    selectedItemOnClick:(String) -> Unit,
    unSelectedItemOnClick:(Int) -> Unit
){
    item {
        SmallBox(
            text = "Filters" ,
            textColor = colorResource(id = R.color.filters_heading_color),
            boxColor = colorResource(id = R.color.filter_title_box_background),
            modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_20))
        )
        VerticalDivider(modifier = Modifier
            .height(dimensionResource(id = R.dimen.height_35))
            .padding(end = dimensionResource(id = R.dimen.padding_5)))
    }
    items(userSelectedOptions){
        SmallBox(
            text = it,
            boxColor = colorResource(id = R.color.filter_selected_button_color),
            textColor = colorResource(id = R.color.filter_button_selected_text_color),
            highlightBox = true,
            onClick = {
                selectedItemOnClick(it)
            }
        )
    }
    items(list.size){
        SmallBox(
            text = list.elementAt(it),
            boxColor = colorResource(id = R.color.filter_buttons_background),
            textColor = colorResource(id = R.color.filter_button_unselected_text_color),
            onClick = {
                unSelectedItemOnClick(it)
            }
        )
    }
}


fun LazyListScope.groceryList(
    filteredList:List<GroceryItemDetails>?,
    homeViewModel: HomeViewModel
){
    smallInfoText(
        text = "*Can Apply Filters or Use Global Search For Faster Access",
    )
    filteredList?.let {
        groceryAndSearchCommonCard(
            it = it ,
            homeViewModel = homeViewModel
        )
    }
}
