package com.rspk.grocerylistapp.compose.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.MainActivity
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.R.drawable as AppIcon
import com.rspk.grocerylistapp.common.composables.CustomTransparentCard
import com.rspk.grocerylistapp.common.composables.CustomUserAnalyticsCard
import com.rspk.grocerylistapp.compose.home.advertise.Advertise
import com.rspk.grocerylistapp.constants.groceryCategories
import com.rspk.grocerylistapp.constants.pagerImages
import com.rspk.grocerylistapp.constants.userAnalyticsCards
import com.rspk.grocerylistapp.model.AppProposedList
import com.rspk.grocerylistapp.model.GroceryItemDetails
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    userCardNavigation: (NavigationRoutes) -> Unit = {},
    proposedList: (AppProposedList) -> Unit = {},
    onCurrentItemsFilteringStringChange: (String) -> Unit,
    onCurrentListChange: (List<String>) -> Unit = {},
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            HomeItems(
                onCurrentItemsFilteringStringChange = onCurrentItemsFilteringStringChange,
                onCurrentListChange = onCurrentListChange,
                homeViewModel = homeViewModel,
                userCardNavigation = userCardNavigation,
                proposedList = proposedList
            )
        }
    }
}


@Composable
fun HomeItems(
    onCurrentItemsFilteringStringChange: (String) -> Unit,
    onCurrentListChange: (List<String>) -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel(),
    userCardNavigation: (NavigationRoutes) -> Unit = {},
    proposedList: (AppProposedList) -> Unit = {},
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Banner()
    GroceryCategoryRow(
        navigate = onCurrentItemsFilteringStringChange,
        onSearchListChange = onCurrentListChange
    )
    HomePageHorizontalPager()
    UserAnalyticsRow(
        navigate = userCardNavigation
    )
    Advertise()
    homeViewModel.bestProductsList?.let {
        if (it.size >= 4) {
            HomeScreenCardTitle(title = stringResource(id = R.string.best_products))
            HomeScreenItemsCard(
                listOfItems = it,
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        proposedList(
                            AppProposedList(
                                proposedList = it,
                                title = "Health is Wealth",
                                subText = "These are Some Best Products for your health",
                                tagColor = Color.Green
                            )
                        )
                    }
                }
            )
        }
    }
    homeViewModel.getFrequentlyBroughtItems?.let {
        if (it.size >= 4) {
            HomeScreenCardTitle(title = stringResource(id = R.string.frequently_bought_items))
            HomeScreenItemsCard(
                listOfItems = it,
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        proposedList(
                            AppProposedList(
                                proposedList = it,
                                title = "Products you like to buy",
                                subText = "According to our data these are the products you buy them frequently",
                            )
                        )
                    }
                },
                tagColor = Color.Transparent
            )
        }
    }
}

