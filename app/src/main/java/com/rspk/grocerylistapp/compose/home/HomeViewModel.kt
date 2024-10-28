package com.rspk.grocerylistapp.compose.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.DocumentSnapshot
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.model.GroceryItemDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.internal.filterList
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fireBaseDatabase: FireBaseDatabase
):MainViewModel(){

    var userCartDetails by mutableStateOf<List<String>?>(null)
    var bestProductsList by mutableStateOf<List<GroceryItemDetails>?>(null)
    var getFrequentlyBroughtItems by mutableStateOf<List<GroceryItemDetails>?>(null)

    init {
        getUserCartItemDetails()
        getAllBestProducts()
        getFrequentlyBroughtItems()
    }

    fun addMonthlyGroceryList(data:GroceryItemDetails) {
        launchCaching {
            fireBaseDatabase.addOrUpdatedUsersCart(data)
        }
    }

    private fun getUserCartItemDetails(){
        launchCaching {
            fireBaseDatabase.getUsersCartItems().collect{
                userCartDetails = it
            }
        }
    }


   private fun getAllBestProducts(){
        launchCachingWithIoDispatcher {
            fireBaseDatabase.getSearchResults("healthy products").collect{
                bestProductsList = it
            }
        }
    }

    private fun getFrequentlyBroughtItems(){
        launchCachingWithDefaultDispatcher {
            fireBaseDatabase.getUserMostlyBoughtProducts()?.collect{
                getFrequentlyBroughtItems = it
            }
        }
    }
}