package com.rspk.grocerylistapp.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.CustomHomePageItemDetailsCard
import com.rspk.grocerylistapp.common.composables.CustomTransparentCard
import com.rspk.grocerylistapp.common.composables.CustomUserAnalyticsCard
import com.rspk.grocerylistapp.constants.getDateFromMillis
import com.rspk.grocerylistapp.constants.groceryCategories
import com.rspk.grocerylistapp.constants.pagerImages
import com.rspk.grocerylistapp.constants.userAnalyticsCards
import com.rspk.grocerylistapp.model.GroceryItemDetails
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat


@Composable
fun Banner() {
    val date by remember { mutableStateOf("Current Date - ${getDateFromMillis()}") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.home_banner_color))
            .height(dimensionResource(id = R.dimen.height_40))
            .padding(start = dimensionResource(id = R.dimen.padding_20)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_7)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = rememberAsyncImagePainter(R.drawable.baseline_calendar_month_24),
            contentDescription = "Date Icon",
            tint = colorResource(id = R.color.home_banner_text_color)
        )
        Text(
            text = date,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            color = colorResource(id = R.color.home_banner_text_color)
        )
    }
}


@Composable
fun GroceryCategoryRow(
    navigate: (String) -> Unit,
    onSearchListChange:(List<String>) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.height_120))
            .background(colorResource(id = R.color.home_category_row_color))
    ) {
        items(groceryCategories, key = { it.text }) {
            val image = remember {  it.image }
            val text = remember { it.text }
            CustomTransparentCard(
                image = image,
                text = text,
                onClick = {
                    onSearchListChange(it.subList)
                    navigate(text)
                }
            )
        }
    }
}

@Composable
fun HomePageHorizontalPager() {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { pagerImages.size })

    LaunchedEffect(key1 = pagerState.currentPage) {
        while (true) {
            delay(10000)
            val nextPage = (pagerState.currentPage + 1) % pagerImages.size
            pagerState.scrollToPage(nextPage)
        }
    }

    val currentColor = remember(pagerState.currentPage) { pagerImages[pagerState.currentPage].second }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.height_270))
            .shadow(
                elevation = 150.dp,
                clip = true,
                spotColor = currentColor,
                ambientColor = currentColor,
            )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = remember(pagerImages[it].first) { pagerImages[it].first },
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )
        }
        ImageTopGradient(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
        PagerDots(pagerState = pagerState)
    }
}

@Composable
private fun ImageTopGradient(
    modifier: Modifier = Modifier
){
    val darkMode = isSystemInDarkTheme()
    val blurColor = remember { Color.Black.copy(alpha = if (darkMode) 0.5f else 0.3f) }
    val gradientBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                blurColor
            )
        )
    }
    val height = remember { (if(darkMode) 270 else 150).dp }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(gradientBrush)
            .blur(16.dp)
    )
}

@Composable
private fun PagerDots(
    pagerState: PagerState
){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.BottomCenter)
    ) {
        repeat(pagerImages.size) { index ->
            Box(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_27),
                        horizontal = dimensionResource(id = R.dimen.padding_5)
                    )
                    .size(dimensionResource(id = R.dimen.size_8))
                    .clip(CircleShape)
                    .background(
                        if (index == pagerState.currentPage) Color.LightGray else Color.White
                    )
            )
        }
    }
}


@Composable
fun UserAnalyticsRow(
    navigate: (NavigationRoutes) -> Unit
){
    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = (-15).dp)
    ) {
        items(userAnalyticsCards, key = {it.text}) {
            val image = remember { it.image }
            val text = remember { it.text }
            val route = remember { it.route }
            CustomUserAnalyticsCard(
                image = image,
                text = text,
                onClick = { navigate(route) },
                customWidth = dimensionResource(id = R.dimen.size_150),
                customHeight = dimensionResource(id = R.dimen.size_200)
            )
        }
    }
}

@Composable
fun HomeScreenCardTitle(
    modifier: Modifier = Modifier,
    title:String
){
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.recommendations_smaller_card_text_color),
        modifier = modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_15))
            .padding(
                top = dimensionResource(id = R.dimen.padding_40),
                bottom = dimensionResource(id = R.dimen.padding_10)
            )
    )
}

@Composable
fun HomeScreenItemsCard(
    modifier: Modifier = Modifier,
    listOfItems: List<GroceryItemDetails>,
    onClick:() -> Unit,
    tagColor: Color = Color.Green
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_10))
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.height_700))
            .clip(RoundedCornerShape(7.dp))
            .background(colorResource(id = R.color.recommendations_main_card_color))
            .padding(dimensionResource(id = R.dimen.padding_10)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_10))
    ) {
        ItemsCardRow(listOfItems = listOfItems, range = 0..<2,modifier=Modifier.weight(1f), tagColor = tagColor)
        ItemsCardRow(listOfItems = listOfItems, range = 2..<4,modifier=Modifier.weight(1f), tagColor = tagColor)
        Text(
            text = "See More",
            modifier = Modifier
                .align(Alignment.Start)
                .padding(
                    vertical = dimensionResource(id = R.dimen.padding_10),
                    horizontal = dimensionResource(id = R.dimen.padding_5)
                )
                .clickable(
                    interactionSource = null,
                    indication = null
                ) {
                    onClick()
                },
            color = colorResource(id = R.color.recommendations_smaller_card_text_color)
        )
    }
}

@Composable
internal fun ItemsCardRow(
    modifier: Modifier = Modifier,
    listOfItems: List<GroceryItemDetails>,
    range: IntRange,
    tagColor: Color
){
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_9))
    ) {
        for (i in range) {
            val rememberedList = remember { listOfItems[i] }
            CustomHomePageItemDetailsCard(
                image = rememberedList.image,
                text = rememberedList.id,
                avgPrice = rememberedList.avgPrice,
                tagColor = tagColor,
                starRating = rememberedList.rating.toFloatOrNull() ?: 0f,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

