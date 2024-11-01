package com.rspk.grocerylistapp.constants

import androidx.compose.ui.graphics.Color
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.model.BottomBar
import com.rspk.grocerylistapp.model.GroceryCategory
import com.rspk.grocerylistapp.model.UserAnalytics
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val year: String = SimpleDateFormat("yyyy" , Locale.getDefault()).format(Date())
val month: String = SimpleDateFormat("MMMM-yyyy" , Locale.getDefault()).format(Date())
val months_list =
    listOf(
        "January-$year",
        "February-$year",
        "March-$year",
        "April-$year",
        "May-$year",
        "June-$year",
        "July-$year",
        "August-$year",
        "September-$year",
        "October-$year",
        "November-$year",
        "December-$year"
    )

val previousMonth = months_list[months_list.indexOf(month) - 1]

val groceryCategories =
    listOf(
    GroceryCategory(
        "Fruits",
        R.drawable.r,
        subList = listOf("pome fruits", "citrus fruits", "berries", "stone fruits", "melons", "tropical fruits", "exotic fruits")
    ),
    GroceryCategory(
        "Vegetables",
        R.drawable.r__1_,
        subList = listOf("alliums", "leafy vegetables", "root vegetables", "cruciferous vegetables", "marrow vegetables", "legumes", "gourds", "herbs")
    ),
    GroceryCategory(
        "Dairy",
        R.drawable.milk_png_1000x752_9f08ee93_transparent,
        subList = listOf("milk products", "cheese varieties", "yogurts", "creams", "butters", "dairy alternatives")
    ),
    GroceryCategory(
        "Bakery",
        R.drawable.pngtree_cute_breads_bakery_stationary_sticker_oil_painting_png_image_13165679,
        subList = listOf("bread varieties", "buns & rolls", "pastries", "muffins", "cakes", "pies & tarts", "cookies")
    ),
    GroceryCategory(
        "Beverages",
        R.drawable._9_598310_cold_drinks_2lt_cool_drinks_images_png,
        subList = listOf("cool drinks", "sodas", "juices", "teas", "coffees", "smoothies", "energy drinks","water")
    ),
    GroceryCategory(
        "Snacks",
        R.drawable.snack,
        subList = listOf("chips", "nuts & seeds", "popcorn", "fruit snacks", "granola bars", "pretzels", "candy", "cookies")
    ),
    GroceryCategory(
        "Cleaning",
        R.drawable.cleaning_items_ai_generated_free_png,
        subList = listOf("surface cleaners", "dish cleaners", "laundry supplies", "glass cleaners", "bathroom cleaners", "disinfectants", "tools & accessories")
    ),
    GroceryCategory(
        "Nuts,Pulses",
        R.drawable.lentil_3216,
        subList = listOf("tree nuts", "beans", "lentils", "peas", "soybeans", "chickpeas", "peanuts")
    ),
    GroceryCategory(
        "Spices",
        R.drawable.pngtree_selection_of_spices_for_christmas_and_thanksgiving_png_image_13664384,
        subList = listOf("ground spices", "whole spices", "herbs & seasonings", "spice mixes", "pepper varieties", "curry blends", "ginger & garlic")
    ),
    GroceryCategory(
        "Flours",
        R.drawable._8fe8f887b0948c02b18820763888e9d,
        subList = listOf("wheat flours", "gluten-free flours", "nut flours", "corn flours", "rice flours", "specialty flours")
    ),
    GroceryCategory(
        "Pooja",
        R.drawable.rathyatra_dhup_480x480,
        subList = listOf("incense", "ghee", "flowers", "camphor", "puja powders", "sacred threads", "offerings", "holy water")
    ),
    GroceryCategory(
        "Other",
        R.drawable.pngtree_grocery_png_image_8919443,
        subList = listOf("fatty fish", "cooking oils", "vinegar", "pasta", "rice", "sauces", "dressings", "soup mixes")
    )
)


val pagerImages = listOf(
    R.drawable.byit_ to Color(0XFF6BAECB),
    R.drawable._451588_3914789 to Color(0xFF9393E0),
    R.drawable.designer to Color(0xFF026879),
    R.drawable._147885844 to Color(0xFF1DD6B9),
    R.drawable.rb_2875 to Color(0xFFF7F5F5),
 )

val userAnalyticsCards =
    listOf(
        UserAnalytics(
            image = R.drawable._606538_5873,
            text = "User Spending's And Analytics",
            route = NavigationRoutes.AnalyticsScreen()
        ),
        UserAnalytics(
            image = R.drawable._6268046_rm373batch12_037,
            text = "Added To Cart Items",
            route = NavigationRoutes.CartScreen()
        ),
        UserAnalytics(
            image = R.drawable._2106271_8979524,
            text = "Past Month Grocery List",
            route = NavigationRoutes.CartScreen(month = previousMonth, showMenu = true)
        ),
        UserAnalytics(
            image = R.drawable.img_20241010_180451_602,
            text = "User Uploaded Bills and Receipts",
            route = NavigationRoutes.AnalyticsScreen(currentComposeState = true)
        ),
    )

val bottomBarList = listOf(
    BottomBar(
        image = R.drawable.outline_home_24,
        text = "Home",
        route = NavigationRoutes.HomeScreen,
    ),
    BottomBar(
        image = R.drawable.outline_shopping_cart_24,
        text = "Cart",
        route = NavigationRoutes.CartScreen(),
    ),
    BottomBar(
        image = R.drawable.outline_show_chart_24,
        text = "Analytics",
        route = NavigationRoutes.AnalyticsScreen(),
    ),
    BottomBar(
        image = R.drawable.outline_person_24,
        text = "Profile",
        route = NavigationRoutes.ProfileScreen,
    )
)

val natural_numbers_list = listOf("1","2","3","4","5")

val integers_numbers_list = listOf("0.25","0.5","1","2","3")

val colorsList = listOf(
    Color(0xFF00796B),
    Color(0xFF80CBC4),
    Color(0xFF26A69A),
    Color(0xFF4CAF50),
    Color(0xFF64B5F6),
    Color(0xFFFFA726),
    Color(0xFFFFEB3B),
    Color(0xFFF06292),
    Color(0xFFEF5350),
    Color(0xFF9E9E9E),
    Color(0xFF607D8B),
    Color(0xFF8E24AA)
)

