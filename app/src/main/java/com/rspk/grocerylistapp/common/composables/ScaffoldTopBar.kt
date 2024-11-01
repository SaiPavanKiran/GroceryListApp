package com.rspk.grocerylistapp.common.composables

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.modifier.customTopAppBarColors
import com.rspk.grocerylistapp.common.modifier.scaffoldTopBarModifier
import com.rspk.grocerylistapp.compose.home.AppProposedList
import com.rspk.grocerylistapp.compose.home.GroceryList
import com.rspk.grocerylistapp.compose.home.SearchScreen
import com.rspk.grocerylistapp.constants.bottomBarList
import com.rspk.grocerylistapp.model.AppProposedList
import com.rspk.grocerylistapp.model.GroceryItemDetails
import com.rspk.grocerylistapp.navigation.NavigationRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.rspk.grocerylistapp.R.drawable as AppIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    modifier: Modifier = Modifier,
    commonViewModel: CommonViewModel = hiltViewModel(),
    bottomBarNavigation: (NavigationRoutes) -> Unit,
    currentRoute: String,
    content: @Composable (
        onCurrentItemsFilteringStringChange: (String) -> Unit,
        onCurrentItemsListChange: (List<String>) -> Unit,
        appProposedListChange: (AppProposedList) -> Unit
    ) -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    CustomBasicTextField(
                        value = commonViewModel.searchTerm,
                        onValueChange = {
                            coroutineScope.launch(Dispatchers.IO) {
                                commonViewModel.searchTerm = it
                                commonViewModel.searchList(it)
                            }
                        },
                        trailingIcon = {
                            if (commonViewModel.searchTerm.isNotEmpty()) {
                                Icon(
                                    painter = painterResource(id = AppIcon.outline_close_small_24),
                                    contentDescription = "Close Icon",
                                    modifier = it
                                        .clickable {
                                            commonViewModel.searchTerm = ""
                                        },
                                    tint = colorResource(id = R.color.top_text_field_text_colors)
                                )
                            }
                        }
                    )
                },
                colors = TopAppBarDefaults.customTopAppBarColors(),
                modifier = modifier.scaffoldTopBarModifier()
            )
        },
        bottomBar = {
            BottomNavigationBar(
                bottomBarNavigation = bottomBarNavigation,
                currentRoute = currentRoute,
                onCurrentItemsFilteringStringChange = {
                    commonViewModel.currentItemsFilteringString = it
                },
                onSearchTermChange = { commonViewModel.searchTerm = it },
                onProposedListChange = { commonViewModel.appProposedList = it },
                onSubListChange = { commonViewModel.subTypeList = it }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ScaffoldContent(
                commonViewModel = commonViewModel,
                content = content
            )
        }
    }
}


@Composable
fun ScaffoldContent(
    commonViewModel: CommonViewModel = hiltViewModel(),
    content: @Composable (
        onCurrentItemsFilteringStringChange: (String) -> Unit,
        onCurrentItemsListChange: (List<String>) -> Unit,
        appProposedListChange: (AppProposedList) -> Unit
    ) -> Unit,

    ) {

    if (commonViewModel.searchTerm.isNotEmpty()) {
        SearchScreen(
            searchList = commonViewModel.searchedList
        )
    } else if (commonViewModel.appProposedList.proposedList.isNotEmpty()) {
        AppProposedList(
            appProposedList = commonViewModel.appProposedList,
            appProposedListChange = { commonViewModel.appProposedList = it }
        )
    } else if (commonViewModel.currentItemsFilteringString.isNotEmpty()) {
        commonViewModel.filteredItems(
            commonViewModel.currentItemsFilteringString,
            commonViewModel.subTypeList
        )
        commonViewModel.boolean =
            commonViewModel.searchedList?.isNotEmpty() == true && commonViewModel.searchedList?.get(
                0
            )?.type == commonViewModel.currentItemsFilteringString.lowercase()
        GroceryList(
            loadingCondition = commonViewModel.boolean,
            filteredList = commonViewModel.searchedList,
            currentSearchList = commonViewModel.currentFilterItemsList,
            onCurrentItemsFilteringStringChange = {
                commonViewModel.currentItemsFilteringString = it
            },
            onSubListChanged = {
                commonViewModel.subTypeList = it
            }
        )
    } else {
        content(
            { commonViewModel.currentItemsFilteringString = it },
            { commonViewModel.currentFilterItemsList = it },
            { commonViewModel.appProposedList = it }
        )
    }
}

