package com.rspk.grocerylistapp.compose.analytics

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.constants.getStringFormattedYear
import com.rspk.grocerylistapp.model.UserAnalyticsCalculations


@Composable
fun SpendingLayoutAnalyze(
    modifier: Modifier = Modifier,
    configuration: Configuration = LocalConfiguration.current,
    data: UserAnalyticsCalculations,
    analyticsViewModel: AnalyticsViewModel,
){
    if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
        CommonGridLayout(
            data = data ,
            analyticsViewModel = analyticsViewModel ,
            modifier = modifier
        )
    }else{
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            SpendingItems(
                data = data,
                modifier = modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(top = dimensionResource(id = R.dimen.padding_30))
            ) {
                PieChartLandscape(
                    data = data.mappedData,
                    onClick = {
                        analyticsViewModel.getAnalyticsData()
                    }
                )
            }
            CommonGridLayout(
                modifier = modifier.weight(1f),
                data = data ,
                analyticsViewModel = analyticsViewModel ,
            )
        }
    }
}


@Composable
fun CommonGridLayout(
    modifier: Modifier = Modifier,
    data: UserAnalyticsCalculations,
    analyticsViewModel: AnalyticsViewModel,
    configuration: Configuration = LocalConfiguration.current
){
    LazyVerticalGrid(
        contentPadding = PaddingValues(
            vertical = dimensionResource(id = R.dimen.padding_30),
            horizontal = dimensionResource(id = R.dimen.padding_15)),
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        smallInfoText(
            text= "*Can Long Press to Navigate between Spending's and Bill's",
            textModifier = Modifier
                .padding(bottom = 5.dp)
        )
        if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            item(span = { GridItemSpan(maxCurrentLineSpan) }){
                SpendingItems(data = data) {
                    PieChartPortrait(
                        data = data.mappedData,
                        onClick = {
                            analyticsViewModel.getAnalyticsData()
                        }
                    )
                }
            }
        }
        monthlyGridData(
            data = data
        )
    }
}

@Composable
fun SpendingItems(
    modifier: Modifier = Modifier,
    data: UserAnalyticsCalculations,
    content : @Composable () -> Unit
){
    Column(
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_10))
    ) {
        Text(
            text = stringResource(id = R.string.yearly_analysis) ,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            color = colorResource(id = R.color.analytics_pie_chart_main_heading_color)
        )
        content()
        TotalAvgSpendingCard(
            totalText = stringResource(id = R.string.total_spending, data.total),
            averageText = stringResource(id = R.string.average_spending, formatDecimal(data.average))
        )
    }
}

@Composable
fun TotalAvgSpendingCard(
    totalText:String,
    averageText:String
){
    Row(
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_15))
            .clip(RoundedCornerShape(9.dp))
            .background(colorResource(id = R.color.analytics_card_background))
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_15)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(0.85f)
        ) {
            Text(
                text = totalText,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_7)),
                color = colorResource(id = R.color.analytics_card_main_text_color),
            )
            Text(
                text = averageText,
                fontSize = 12.sp,
                color = colorResource(id = R.color.analytics_card_small_text_color)
            )
        }
        Text(
            text = getStringFormattedYear(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.analytics_card_small_text_color),
            modifier = Modifier.weight(0.15f),
        )
    }
}


fun LazyGridScope.monthlyGridData(
    data: UserAnalyticsCalculations
){
    item(span = { GridItemSpan(maxCurrentLineSpan) }) {
        Text(
            text = "Monthly Analysis:" ,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_10)),
            color = colorResource(id = R.color.analytics_pie_chart_main_heading_color)
        )
    }
    items(data.mappedData.size , key = { data.mappedData.keys.elementAt(it) }){
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_5))
                .clip(RoundedCornerShape(7.dp))
                .defaultMinSize(minHeight = 170.dp)
                .background(colorResource(id = R.color.analytics_card_background))
                .padding(dimensionResource(id = R.dimen.padding_10)),
        ) {
            Text(
                text = data.mappedData.keys.elementAt(it),
                fontSize = 12.sp,
                color = colorResource(id = R.color.analytics_card_small_text_color),
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.padding_5))
                    .weight(1f)
            )
            Text(
                text = "Total:\n${data.allMonthTotals.elementAt(it)}",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.analytics_card_main_text_color),
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.padding_5))
                    .weight(3f)
            )
            Text(
                text = "Avg: ₹${formatDecimal(data.mappedData.values.elementAt(it).toString())}",
                fontSize = 14.sp,
                color = colorResource(id = R.color.analytics_card_small_text_color),
                modifier = Modifier
                    .padding(bottom = dimensionResource(id = R.dimen.padding_5))
                    .weight(1f)
            )
        }
    }
}