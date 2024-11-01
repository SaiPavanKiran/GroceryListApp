package com.rspk.grocerylistapp.firebase.impl

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.dataObjects
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.constants.getInstallationId
import com.rspk.grocerylistapp.constants.getStringFormattedFullDate
import com.rspk.grocerylistapp.constants.getStringFormattedMonth
import com.rspk.grocerylistapp.constants.getStringFormattedMonthPlusYear
import com.rspk.grocerylistapp.constants.getStringFormattedYear
import com.rspk.grocerylistapp.constants.months_list
import com.rspk.grocerylistapp.constants.year
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.model.GroceryItemDetails
import com.rspk.grocerylistapp.model.OwnerData
import com.rspk.grocerylistapp.model.UserInfo
import com.rspk.grocerylistapp.model.UserInfoUpdate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FireBaseDatabaseImpl @Inject constructor(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : FireBaseDatabase {

    /////////////////////******** Adding User Data *************\\\\\\\\\\\\\\\\\\\\\
    override suspend fun addUsersInfo(userInfo: UserInfo, ownerData: OwnerData) {
        try {
            val firebaseInstallationId = getInstallationId()
            val setUsersInfo = fireStore.collection(USERS_INFO).document(auth.currentUser?.uid!!)
            val setDataForOwner = fireStore.collection(OWNER).document(auth.currentUser?.uid!!)
            val fireStoreRef =
                fireStore.collection(USERS_INFO).document(auth.currentUser?.uid!!).get().await()
            val accountCreationDate = fireStoreRef.get("accountCreationDate") as? String
            val adminValue = fireStoreRef.get("admin") as? Boolean ?: false
            fireStore.runBatch {
                it.set(
                    setUsersInfo, userInfo.copy(
                        firebaseInstallationId = firebaseInstallationId,
                        accountCreationDate = accountCreationDate ?: getStringFormattedFullDate(),
                        lastLoginDate = getStringFormattedFullDate(),
                        admin = adminValue
                    )
                )
                it.set(setDataForOwner, ownerData.copy(isAdmin = adminValue))
            }.await()
        } catch (ex: Exception) {
            Log.e(TAG, "Error While Adding User Info: ${ex.message}")
        }
    }


    //////////// ****** Sign In Related *****\\\\\\\\\\\\\\\\\
    override suspend fun updateUsersInfo(userInfo: UserInfoUpdate) {
        try {
            fireStore
                .collection(USERS_INFO)
                .document(auth.currentUser?.uid!!)
                .update(mappedUserData(userInfo))
                .await()
        } catch (ex: Exception) {
            Log.e(TAG, "Error While Updating User Info When Sign In: ${ex.message}")
        }
    }

    private fun mappedUserData(userInfo: UserInfoUpdate): Map<String, String?> {
        return mapOf(
            "userName" to userInfo.userName,
            "userBio" to userInfo.userBio,
            "phoneNumber" to userInfo.phoneNumber,
            "email" to userInfo.email,
            "photoUrl" to userInfo.photoUrl,
        )
    }

    ///////// ******* Login Related ****** \\\\\\\\\\\
    override suspend fun updateLastLoginDate() {
        try {
            fireStore
                .collection(USERS_INFO)
                .document(auth.currentUser?.uid!!)
                .update("lastLoginDate", getStringFormattedFullDate())
                .await()
        } catch (ex: Exception) {
            Log.e(TAG, "Error While Updating Last Login Date: ${ex.message}")
        }
    }

    override suspend fun getUserInfo(): UserInfo? {
        try {
            val snapshot = fireStore.collection(USERS_INFO)
                .document(auth.currentUser?.uid!!)
                .get(Source.DEFAULT)
                .await()
            return snapshot.toObject(UserInfo::class.java)
        } catch (ex: Exception) {
            Log.e(TAG, "Error While Getting User Info: ${ex.message}")
            return null
        }
    }

    override suspend fun deleteAccountData() {
        val userMainDocument =
            fireStore.collection(MONTHLY_GROCERIES).document(auth.currentUser?.uid!!)
        val setUsersInfo = fireStore.collection(USERS_INFO).document(auth.currentUser?.uid!!)
        val setDataForOwner = fireStore.collection(OWNER).document(auth.currentUser?.uid!!)
        try {
            fireStore.runBatch { batch ->
                batch.delete(userMainDocument)
                batch.delete(setUsersInfo)
                batch.delete(setDataForOwner)
            }.await()
        } catch (e: Exception) {
            Log.e(TAG, "Error While Deleting Account Data: ${e.message}")
        }
    }


    //////Cart Related
    //////////////////// ******* -----> Add Product to Cart or Update it <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun addOrUpdatedUsersCart(data: GroceryItemDetails) {
        try {
            val ref = fireStore.collection(MONTHLY_GROCERIES)
                .document(auth.currentUser?.uid!!)

            val items = ref.get().await().data?.keys
            val mappedData = groceryItemToMap(data)
            if (items != null) {
                if (items.contains(data.id)) {
                    val itemRef = ref.get().await().data?.get(data.id) as? Map<String, String>
                    val previousQuantity = itemRef?.get("quantity")?.toDouble() ?: 0.0
                    val timeStamp = itemRef?.get("time_stamp") ?: ""
                    ref.update(groceryItemToMap(data, previousQuantity, timeStamp)).await()
                } else {
                    ref.update(mappedData).await()
                }
            } else {
                ref.set(mappedData).await()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error While Adding Product To Cart: ${ex.message}")
        }
    }

    //^^^^^^^^^^^^^^ Above Function Util Private Function ^^^^^^^^^^^^^^\\
    private fun groceryItemToMap(
        item: GroceryItemDetails,
        previousQuantity: Double = 0.0,
        time: String = "",
        currentMonth: String = ""
    ): Map<String, Map<String, String>> {

        val month = if (currentMonth == "") getStringFormattedMonthPlusYear() else currentMonth
        val currentTimeInMillis = if (time == "") System.currentTimeMillis().toString() else time

        val itemDataMap = mapOf(
            "description" to item.description,
            "image" to item.image,
            "avg_price" to item.avgPrice,
            "quantity_type" to item.quantityType,
            "star_count" to item.rating,
            "quantity" to (item.quantity + previousQuantity).toString(),
            "time_stamp" to currentTimeInMillis,
            "current_month" to month,
            "is_checked" to item.isChecked.toString()
        )

        return mapOf(item.id to itemDataMap)
    }

    //////////////////// ******* -----> Cart Items Listener In Real Time <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun getUsersCartItems(): Flow<List<String>?> =
        callbackFlow {
            val currentMonth = getStringFormattedMonth()
            var listener: ListenerRegistration? = null
            try {
                listener = fireStore.collection(MONTHLY_GROCERIES)
                    .document(auth.currentUser?.uid!!)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            Log.e(TAG, "Error occurred_Listener_2: ${error.message}", error)
                        }
                        val filteredItems = value?.data?.keys?.filter {
                            it.substringAfter("(").substringBefore(")").trim()
                                .lowercase() == currentMonth.lowercase()
                        }
                        trySend(filteredItems?.toList())
                    }
            } catch (ex: Exception) {
                Log.e(TAG, "Error occurred While Getting User Cart Items: ${ex.message}", ex)
            }
            awaitClose { listener?.remove() }
        }


    //////////////////// ******* -----> Fetch All Cart Items <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun getFilteredCartItems(month: String): Flow<List<GroceryItemDetails>?> =
        callbackFlow {
            var listener: ListenerRegistration? = null
            try {
                listener = fireStore.collection(MONTHLY_GROCERIES)
                    .document(auth.currentUser?.uid!!)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            Log.e(TAG, "Error occurred_Listener_3: ${error.message}", error)
                        }
                        val groceryItems = value?.data as? Map<String, Map<String, String>>
                        val filteredItems = groceryItems?.filter {
                            (it.value["current_month"] ?: "") == month
                        }
                        val groceryItemsList = filteredItems?.mapNotNull {
                            GroceryItemDetails(
                                id = it.key,
                                description = it.value["description"] ?: "",
                                type = it.value["type"] ?: "",
                                subType = it.value["sub_type"] ?: "",
                                image = it.value["image"] ?: "",
                                avgPrice = it.value["avg_price"] ?: "",
                                quantityType = it.value["quantity_type"] ?: "",
                                quantity = it.value["quantity"]?.toFloat() ?: 0f,
                                rating = it.value["star_count"] ?: "",
                                isChecked = it.value["is_checked"]?.toBoolean() ?: false,
                                timeStamp = it.value["time_stamp"] ?: "",
                                currentMonth = it.value["current_month"] ?: ""
                            )
                        }?.sortedByDescending { it.timeStamp }
                        trySend(groceryItemsList)
                    }
            } catch (ex: Exception) {
                Log.e(TAG, "Error occurred While Getting Filtered Cart Items: ${ex.message}", ex)
            }
            awaitClose { listener?.remove() }
        }

    //////////////////// ******* -----> Update Cart With New Data <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun updateCartStatus(data: GroceryItemDetails) {
        try {
            fireStore.collection(MONTHLY_GROCERIES)
                .document(auth.currentUser?.uid!!)
                .update(
                    groceryItemToMap(
                        data,
                        time = data.timeStamp,
                        currentMonth = data.currentMonth
                    )
                )
                .await()
        } catch (ex: Exception) {
            Log.e(TAG, "Error occurred While Updating Cart Status: ${ex.message}", ex)
        }
    }

    //////////////////// ******* -----> Update Cart With New Quantity <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun deleteItem(dataId: String) {
        try {
            fireStore.collection(MONTHLY_GROCERIES)
                .document(auth.currentUser?.uid!!)
                .update(mapOf(dataId to FieldValue.delete()))
                .await()
        } catch (ex: Exception) {
            Log.e(TAG, "Error occurred While Deleting Item: ${ex.message}", ex)
        }
    }

    //////////////////// ******* -----> Calculate Avg Price <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun calculateAvgPrice(month: String): String {
        try {
            val ref = fireStore.collection(MONTHLY_GROCERIES).document(auth.currentUser?.uid!!)
            val data = ref.get().await().data as? Map<String, Map<String, String>>
            var totalMinPrice = 0.0
            var totalMaxPrice = 0.0
            data?.filter {
                it.value["current_month"] == month
            }?.forEach {
                val minAvgPrice =
                    it.value["avg_price"]?.substringAfter("₹")?.substringBefore("-")?.toDouble()
                        ?: 0.0
                val maxAvgPrice =
                    it.value["avg_price"]?.substringAfter("-₹")?.substringBefore(" ")
                        ?.toDouble() ?: 0.0
                val quantity = it.value["quantity"]?.toDouble() ?: 0.0
                val isChecked = it.value["is_checked"]?.toBoolean() ?: false
                if (isChecked) {
                    totalMinPrice += minAvgPrice * quantity
                    totalMaxPrice += maxAvgPrice * quantity
                }
            }

            val analyticsData = data?.get(USER_ANALYTICS) ?: emptyMap()
            ref.update(
                mapOf(
                    USER_ANALYTICS to addMonthlyData(
                        analyticsData,
                        month,
                        if (totalMinPrice == 0.0 && totalMaxPrice == 0.0) "₹0" else
                            "₹${formatDecimal(totalMinPrice.toString())} - ₹${
                                formatDecimal(
                                    totalMaxPrice.toString()
                                )
                            }"
                    )
                )
            )
            return if (totalMinPrice == 0.0 && totalMaxPrice == 0.0) "₹0" else
                "₹${formatDecimal(totalMinPrice.toString())} - ₹${formatDecimal(totalMaxPrice.toString())}"
        } catch (ex: Exception) {
            Log.e(TAG, "Error occurred While Calculating Avg Price: ${ex.message}", ex)
            return "₹0"
        }
    }


    ////^^^^ above function Util Function^^^\\\\
    private fun addMonthlyData(
        data: Map<String, String>,
        month: String,
        monthData: String
    ): Map<String, String> {
        val newData = data.toMutableMap()
        newData[month] = monthData
        return newData
    }

    //////////////////// ******* -----> Head For Filtering Items With Pre-Defined Small Boxes<------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun getFilteredItems(
        filter: String,
        subtype: List<String>
    ): Flow<List<GroceryItemDetails>> =
        callbackFlow {
            var listener: ListenerRegistration? = null
            try {
                listener = fireStore.collection(ADMIN)
                    .document(ADMIN_DOCUMENT_ID_FOR_USER_GROCERY_LIST)
                    .addSnapshotListener { value, error ->
                        if (error != null) {
                            Log.e("TesTingProgress", "Error occurred4: ${error.message}", error)
                            close(error)
                            return@addSnapshotListener
                        }
                        val groceryItems = value?.data as? Map<String, Map<String, String>>
                        val filteredGroceryItems = groceryItems?.filter { data ->
                            val itemType = data.value["type"] ?: ""
                            val itemSubType = data.value["sub_type"] ?: ""

                            val isTypeMatch = itemType == filter.lowercase()

                            val isSubtypeMatch = if (subtype.isNotEmpty()) {
                                subtype.any { it.lowercase() == itemSubType }
                            } else {
                                true
                            }

                            isTypeMatch && isSubtypeMatch
                        }?.mapNotNull { filteredItem ->
                            GroceryItemDetails(
                                id = filteredItem.key,
                                description = filteredItem.value["description"] ?: "",
                                type = filteredItem.value["type"] ?: "",
                                subType = filteredItem.value["sub_type"] ?: "",
                                image = filteredItem.value["image"] ?: "",
                                avgPrice = filteredItem.value["avg_price"] ?: "",
                                quantityType = filteredItem.value["quantity_type"] ?: "",
                                rating = filteredItem.value["star_count"] ?: "",
                            )
                        } ?: emptyList()
                        trySend(filteredGroceryItems).isSuccess
                    }
            } catch (ex: Exception) {
                Log.e(TAG, "Error occurred While Getting Filtered Items: ${ex.message}", ex)
            }
            awaitClose { listener?.remove() }
        }

    //////////////////// ******* -----> User Mostly Bought Products <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun getUserMostlyBoughtProducts(): Flow<List<GroceryItemDetails>?>? =
        callbackFlow {
            try {
                val userMonthlyGroceryRef = fireStore.collection(MONTHLY_GROCERIES)
                    .document(auth.currentUser?.uid!!)
                    .get()
                    .await()
                val outputData = userMonthlyGroceryRef?.data?.keys
                val outputData1 = outputData?.filter {
                    it != USER_ANALYTICS
                }
                val dictionary = mutableMapOf<String, Int>()
                outputData1?.forEach { item ->
                    val key = item.substringBefore("(").trim()
                    dictionary[key] = dictionary.getOrDefault(key, 0) + 1
                }
                val sortedItems =
                    dictionary.entries.sortedByDescending { it.value }.take(25).map { it.key }
                val groceryItemDetailsList = mutableListOf<GroceryItemDetails>()
                sortedItems.forEach { key ->
                    val groceryDetails = userMonthlyGroceryRef?.data?.entries?.firstOrNull {
                        it.key.contains(key, ignoreCase = true)
                    }?.value as? Map<String, String>

                    groceryDetails?.let {
                        groceryItemDetailsList.add(
                            GroceryItemDetails(
                                id = key,
                                image = it["image"] ?: "",
                                description = it["description"] ?: "",
                                avgPrice = it["avg_price"] ?: "",
                                quantityType = it["quantity_type"] ?: "",
                                rating = it["star_count"] ?: ""
                            )
                        )
                    }
                }
                trySend(groceryItemDetailsList).isSuccess
            } catch (e: Exception) {
                Log.e(TAG, "Error occurred While getting User Mostly Bought Products: ${e.message}", e)
            }
            awaitClose()
        }

    //////////////////// ******* -----> Head For Searching through Search Box <------ ******* \\\\\\\\\\\\\\\\\\\\\\\
    override suspend fun getSearchResults(query: String): Flow<List<GroceryItemDetails>> =
        callbackFlow {
            try {
                Log.d(TAG, "Query: $query")
                val retrievedList = fireStore.collection(ADMIN)
                    .document(ADMIN_DOCUMENT_ID_FOR_USER_GROCERY_LIST)
                    .get()
                    .await()

                val outputData = retrievedList?.data as? Map<String, Map<String, String>>
                val filteredData = outputData?.filter { data ->
                    if(query.isEmpty() || query.isBlank()) return@filter false
                    val searchKeywords = data.value["search_keywords"]?.split(",") ?: emptyList()
                        data.key.contains(query, ignoreCase = true)||
                        data.key.replace(" ","").contains(query.replace(" ",""), ignoreCase = true) ||
                        data.value["type"]?.contains(query, ignoreCase = true) == true ||
                        data.value["sub_type"]?.contains(query, ignoreCase = true) == true ||
                        data.value["star_count"] == query ||
                        data.value["avg_price"]?.substringAfter("₹")?.substringBefore("-₹") == query ||
                        data.value["avg_price"]?.substringAfter("-₹")?.substringBefore(" per") == query ||
                        searchKeywords.any { it.replace(" ","").contains(query, ignoreCase = true) }
                }
                val filteredGroceryItems = filteredData?.mapNotNull { filteredItem ->
                    GroceryItemDetails(
                        id = filteredItem.key,
                        description = filteredItem.value["description"] ?: "",
                        type = filteredItem.value["type"] ?: "",
                        subType = filteredItem.value["sub_type"] ?: "",
                        image = filteredItem.value["image"] ?: "",
                        avgPrice = filteredItem.value["avg_price"] ?: "",
                        quantityType = filteredItem.value["quantity_type"] ?: "",
                        rating = filteredItem.value["star_count"] ?: "",
                    )
                } ?: emptyList()

                trySend(filteredGroceryItems).isSuccess
            } catch (ex: Exception) {
                Log.e(TAG, "Error occurred While Getting Search Results: ${ex.message}", ex)
            }
            awaitClose()
        }


    override suspend fun getUserAnalytics(): Flow<Map<String, String>?> =
        callbackFlow {
            val currentYear = getStringFormattedYear()
            try {
                val data = fireStore.collection(MONTHLY_GROCERIES)
                    .document(auth.currentUser?.uid!!)
                    .get()
                    .await()

                val outputData = data.get(USER_ANALYTICS) as? Map<String, String>
                val filteredData = outputData?.filter {
                    it.key.contains(currentYear)
                }?.toSortedMap(compareBy { months_list.indexOf(it) })
                trySend(filteredData).isSuccess

            } catch (ex: Exception) {
                Log.e(TAG, "Error occurred While Getting User Analytics: ${ex.message}", ex)
            }
            awaitClose()
        }


    companion object {
        private const val TAG = "FireStoreRelatedError"
        private const val ADMIN = "admin"
        private const val ADMIN_DOCUMENT_ID_FOR_USER_GROCERY_LIST = "list_of_groceries"
        private const val USERS_INFO = "users_info"
        private const val OWNER = "owner"
        private const val MONTHLY_GROCERIES = "monthly_groceries"
        private const val USER_ANALYTICS = "user_analytics"
    }
}


