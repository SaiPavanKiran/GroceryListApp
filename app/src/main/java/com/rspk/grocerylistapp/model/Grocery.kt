package com.rspk.grocerylistapp.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import kotlinx.serialization.Serializable
import java.util.Date

data class GroceryCategory(
    val text: String = "",
    @DrawableRes val image: Int = R.drawable.r,
    val subList:List<String>
)

data class GroceryItemDetails(
    val id:String = "",
    val description:String = "",
    val type:String = "",
    val subType:String = "",
    val image:String = "",
    val avgPrice:String = "",
    val quantityType:String = "",
    val rating:String = "",
    val quantity:Float = 0f,
    val isChecked:Boolean = true,
    val timeStamp:String = "",
    val currentMonth:String = ""
)

data class UserAnalytics(
    val text: String = "",
    @DrawableRes val image: Int = R.drawable.r,
    val route: NavigationRoutes
)

data class UserAnalyticsCalculations(
    val total:String = "",
    val average:String = "",
    val mappedData:Map<String,Float> = emptyMap(),
    val allMonthTotals:List<String> = emptyList()
)

data class AppProposedList(
    val proposedList:List<GroceryItemDetails> = emptyList(),
    val title:String = "",
    val subText:String = "",
    val tagColor: Color = Color.Transparent
)