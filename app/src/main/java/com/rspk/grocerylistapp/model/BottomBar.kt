package com.rspk.grocerylistapp.model

import androidx.annotation.DrawableRes
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.navigation.NavigationRoutes

data class BottomBar(
    val text:String = "",
    @DrawableRes val image:Int,
    val route:NavigationRoutes
)