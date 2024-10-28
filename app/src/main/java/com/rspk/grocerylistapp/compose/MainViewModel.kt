package com.rspk.grocerylistapp.compose

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rspk.grocerylistapp.common.snackbar.SnackBarManager
import com.rspk.grocerylistapp.common.snackbar.SnackBarMessage.Companion.toSnackMessage
import com.rspk.grocerylistapp.constants.CurrentButtonLoading
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class MainViewModel :ViewModel() {

    var currentButtonLoading by mutableStateOf(CurrentButtonLoading.NONE)

    fun launchCaching(block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                currentButtonLoading = CurrentButtonLoading.NONE
                SnackBarManager.showMessage(throwable.toSnackMessage())
            },
            block = block,
        )
    }

    fun launchCachingWithIoDispatcher(block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(
            context = Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
                currentButtonLoading = CurrentButtonLoading.NONE
                SnackBarManager.showMessage(throwable.toSnackMessage())
            },
            block = block
        )
    }

    fun launchCachingWithDefaultDispatcher(block: suspend CoroutineScope.() -> Unit){
        viewModelScope.launch(
            context = Dispatchers.Default + CoroutineExceptionHandler { _, throwable ->
                currentButtonLoading = CurrentButtonLoading.NONE
                SnackBarManager.showMessage(throwable.toSnackMessage())
            },
            block = block
        )
    }
}