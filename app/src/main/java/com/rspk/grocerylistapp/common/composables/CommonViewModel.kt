package com.rspk.grocerylistapp.common.composables

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.groceryCategories
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.model.AppProposedList
import com.rspk.grocerylistapp.model.GroceryItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommonViewModel @Inject constructor(
    private val fireBaseDatabase: FireBaseDatabase
):MainViewModel() {

    var searchedList by mutableStateOf<List<GroceryItemDetails>?>(null)
    var searchTerm by mutableStateOf("")
    var currentItemsFilteringString by mutableStateOf("")
    var currentFilterItemsList by  mutableStateOf(listOf<String>())
    var boolean by mutableStateOf(false)
    var subTypeList by mutableStateOf(listOf<String>())
    var appProposedList by mutableStateOf(AppProposedList())

    fun searchList(query:String){
        try {
            launchCaching {
                fireBaseDatabase.getSearchResults(query).collect{
                    searchedList = it
                }
            }
        }catch (ex:Exception){
            Log.e(TAG, "Error Occurred at search query", ex)
        }
    }

    fun filteredItems(filter:String,subTypeList:List<String>){
        try {
            launchCaching {
                fireBaseDatabase.getFilteredItems(filter,subTypeList).collect{
                    searchedList = it
                }
            }
        }catch (ex:Exception){
            Log.e(TAG, "Error Occurred at filtered items", ex)
        }
    }


    companion object{
        private const val TAG = "CommonViewModel"
    }
}