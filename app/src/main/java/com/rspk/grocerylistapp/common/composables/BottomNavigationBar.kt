package com.rspk.grocerylistapp.common.composables

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.constants.bottomBarList
import com.rspk.grocerylistapp.model.AppProposedList
import com.rspk.grocerylistapp.model.GroceryItemDetails
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun BottomNavigationBar(
    bottomBarNavigation: (NavigationRoutes) -> Unit,
    currentRoute: String,
    onCurrentItemsFilteringStringChange :(String) -> Unit,
    onProposedListChange : (AppProposedList) -> Unit,
    onSearchTermChange : (String) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    NavigationBar(
        modifier = Modifier
            .height(dimensionResource(id = R.dimen.size_55)),
        containerColor = colorResource(id = R.color.bottom_bar_container_color),
        contentColor = colorResource(id = R.color.bottom_bar_content_color)
    ) {
        bottomBarList.forEach {
            CustomNavigationBarItem(
                icon = it.image,
                label = it.text,
                selected = it.route.toString().substringBefore("(") == currentRoute,
                onClick = {
                    scope.launch {
                        if(it.route.toString().substringBefore("(") != currentRoute){
                            bottomBarNavigation(it.route)
                        }
                        else{
                            onSearchTermChange("")
                            onCurrentItemsFilteringStringChange("")
                            onProposedListChange(AppProposedList())
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CustomNavigationBarItem(
    modifier: Modifier = Modifier,
    icon: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
){
    Column(
        modifier = modifier
            .background(Color.Transparent)
            .clickable(
                interactionSource = null,
                indication = null
            ) {
                onClick()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(selected) {
            Box(
                modifier = Modifier
                    .size(
                        width = dimensionResource(id = R.dimen.size_30),
                        height = dimensionResource(id = R.dimen.size_2)
                    )
                    .background(
                        colorResource(id = R.color.bottom_bar_selected_color),
                        RoundedCornerShape(20.dp)
                    )
            )
        }
        Spacer(modifier = Modifier.weight(0.25f))
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            modifier = Modifier.size(dimensionResource(id = R.dimen.size_22))
        )
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight(350),
            modifier = Modifier
                .wrapContentSize(unbounded = true)
        )
    }
}