package com.rspk.grocerylistapp.compose.cartscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.getStringFormattedMonthPlusYear
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.model.GroceryItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val fireBaseDatabase: FireBaseDatabase
):MainViewModel() {

    var allCartItems by mutableStateOf<List<GroceryItemDetails>?>(null)
        private set
    var calculatedAvgPrice by mutableStateOf("")
        private set

    private val currentMonth = getStringFormattedMonthPlusYear()

    init {
        getFilteredCartItems(currentMonth)
    }

    fun getFilteredCartItems(month: String) {
        launchCaching {
           fireBaseDatabase.getFilteredCartItems(month).collect{
               allCartItems = it
           }
        }
    }

    fun calculateAvgPrice(month: String){
        launchCaching {
            calculatedAvgPrice = fireBaseDatabase.calculateAvgPrice(month)
        }
    }

    fun updateCartStatus(groceryItemDetails: GroceryItemDetails){
        launchCaching {
            fireBaseDatabase.updateCartStatus(groceryItemDetails)
        }
    }

    fun deleteItem(groceryItem:String){
        launchCaching {
            fireBaseDatabase.deleteItem(groceryItem)
        }
    }
}