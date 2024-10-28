package com.rspk.grocerylistapp.compose.analytics

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rspk.grocerylistapp.compose.MainViewModel
import com.rspk.grocerylistapp.constants.formatDecimal
import com.rspk.grocerylistapp.firebase.FireBaseDatabase
import com.rspk.grocerylistapp.firebase.FireBaseStorage
import com.rspk.grocerylistapp.model.UserAnalyticsCalculations
import com.rspk.grocerylistapp.model.UserStorageInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val fireBaseDatabase: FireBaseDatabase,
    private val fireBaseStorage: FireBaseStorage
):MainViewModel() {

    var analyticsData by mutableStateOf<Map<String,String>?>(null)
    var calculatedSpaceOccupied by mutableFloatStateOf(0f)
    var uploadedImagesList by mutableStateOf<List<UserStorageInfo>?>(null)

    init{
        getAnalyticsData()
        getUploadedImagesList()
        getAmountUsed()
    }

    fun getAnalyticsData(){
        launchCaching {
            fireBaseDatabase.getUserAnalytics().collect{
                analyticsData = it
            }
        }
    }

    fun calculateTotalSpending(
        analyticsData: Map<String,String>?
    ): UserAnalyticsCalculations {
        var totalAverage = 0.0f
        var totalMinvalue = 0.0f
        var totalMaxvalue = 0.0f
        val newMappedData: MutableMap<String, Float> = mutableMapOf()
        val allTotalsList:MutableList<String> = mutableListOf()
        analyticsData?.let {
            it.entries.forEach { entry ->
                val minvalue = entry.value.substringAfter("₹").substringBefore(" -")
                val maxvalue = entry.value.substringAfterLast("₹")
                totalMinvalue += minvalue.toFloat()
                totalMaxvalue += maxvalue.toFloat()
                val avg = (minvalue.toFloat() + maxvalue.toFloat()) / 2
                totalAverage += avg
                if(avg != 0f) {
                    newMappedData[entry.key.substring(0, 3)] = avg
                    allTotalsList.add(entry.value)
                }
            }
        }
        return UserAnalyticsCalculations(
            total = "₹${formatDecimal(totalMinvalue.toString())} - ₹${formatDecimal(totalMaxvalue.toString())}",
            average = "₹${formatDecimal(totalAverage.toString())}",
            mappedData = newMappedData,
            allMonthTotals = allTotalsList
        )
    }

    suspend fun uploadImage(imageUri: Uri)=
            fireBaseStorage.uploadBill(imageUri)


    fun getAmountUsed(){
        launchCaching {
            calculatedSpaceOccupied = fireBaseStorage.calculateUserBucketFilledSize()
        }
    }

    fun getUploadedImagesList(){
        launchCaching {
            fireBaseStorage.getAllUserUploadedImages().collect{
                uploadedImagesList = it
            }
        }
    }

    suspend fun downloadImage(context: Context, fileName: String, imageUri:String) =
        fireBaseStorage.downloadImage(context, fileName, imageUri)

    suspend fun deleteImage(imageUri: String) =
        fireBaseStorage.deleteImage(imageUri)
}