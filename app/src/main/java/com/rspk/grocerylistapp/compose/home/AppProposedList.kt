package com.rspk.grocerylistapp.compose.home

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.AppProposedListTitle
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.model.AppProposedList
import com.rspk.grocerylistapp.model.GroceryItemDetails

@Composable
fun AppProposedList(
    appProposedList: AppProposedList,
    appProposedListChange: (AppProposedList) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    BackHandler {
        appProposedListChange(AppProposedList())
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_10)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AppProposedListTitle(
                text = appProposedList.title
            )
            AppProposedListTitle(
                text = appProposedList.subText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            HorizontalDivider(
                Modifier
                    .padding(dimensionResource(id = R.dimen.padding_20))
                    .size(
                        width = dimensionResource(id = R.dimen.size_250),
                        height = dimensionResource(id = R.dimen.size_0_2)
                    ),
                color = Color.LightGray,
            )
        }
        groceryAndSearchCommonCard(
            it = appProposedList.proposedList ,
            homeViewModel = homeViewModel,
            tagColor = appProposedList.tagColor
        )
    }
}