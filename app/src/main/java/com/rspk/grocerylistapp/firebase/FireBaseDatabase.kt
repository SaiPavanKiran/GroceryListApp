package com.rspk.grocerylistapp.firebase

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.rspk.grocerylistapp.model.GroceryCategory
import com.rspk.grocerylistapp.model.GroceryItemDetails
import com.rspk.grocerylistapp.model.OwnerData
import com.rspk.grocerylistapp.model.UserInfo
import com.rspk.grocerylistapp.model.UserInfoUpdate
import kotlinx.coroutines.flow.Flow

interface FireBaseDatabase {

    suspend fun addUsersInfo(userInfo: UserInfo, ownerData: OwnerData)
    suspend fun getUserInfo(): UserInfo?
    suspend fun updateLastLoginDate()
    suspend fun updateUsersInfo(userInfo: UserInfoUpdate)
    suspend fun deleteAccountData()


    suspend fun getUsersCartItems(): Flow<List<String>?>
    suspend fun addOrUpdatedUsersCart(data:GroceryItemDetails)
    suspend fun getFilteredCartItems(month:String): Flow<List<GroceryItemDetails>?>
    suspend fun updateCartStatus(data: GroceryItemDetails)
    suspend fun deleteItem(dataId: String)
    suspend fun calculateAvgPrice(month: String): String


    suspend fun getFilteredItems(filter: String, subtype: List<String> = emptyList()): Flow<List<GroceryItemDetails>>
    suspend fun getSearchResults(query: String): Flow<List<GroceryItemDetails>>


    suspend fun getUserMostlyBoughtProducts(): Flow<List<GroceryItemDetails>?>?
    suspend fun getUserAnalytics(): Flow<Map<String, String>?>
}