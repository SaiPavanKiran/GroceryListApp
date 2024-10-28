package com.rspk.grocerylistapp.compose.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomDropDownTextField
import com.rspk.grocerylistapp.common.composables.SearchResultsItemDetailCard
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.model.GroceryItemDetails

@Composable
fun SearchScreen(
    searchList: List<GroceryItemDetails>?,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
   LazyColumn(
       contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_20)),
       horizontalAlignment = Alignment.CenterHorizontally,
       modifier = Modifier.fillMaxSize()
   ) {
       smallInfoText(
           text= "*Searches Can Be InAccurate rarely\nPlease Kindly Bear With us",
           textModifier = Modifier
               .padding(bottom = 5.dp)
       )
       searchList?.let {
           groceryAndSearchCommonCard(
               it = it ,
               homeViewModel = homeViewModel,
           )
       }
   }
}