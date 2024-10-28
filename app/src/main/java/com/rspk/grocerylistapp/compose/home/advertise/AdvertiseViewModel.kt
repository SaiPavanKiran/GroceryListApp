package com.rspk.grocerylistapp.compose.home.advertise

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.rspk.grocerylistapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvertiseViewModel @Inject constructor(
    @ApplicationContext val context: Context
):ViewModel() {

    var loading by mutableStateOf(true)
        private set

    var nativeAd: NativeAd? by mutableStateOf(null)
        private set

    init {
        loading()
    }

    private fun loading(){
        viewModelScope.launch(Dispatchers.IO) {
            loadAdvertisement()
            loading = false
        }
    }

    private fun loadAdvertisement(){
        val adLoader = AdLoader.Builder(context, context.getString(R.string.test_ad_unit_id_native_ad))
            .forNativeAd { ad:NativeAd ->
                nativeAd = ad
            }
            .withAdListener(
                object : AdListener(){
                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Log.d("AdLoader", "Ad failed to load: ${p0.message}")
                    }
                }
            )
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setMediaAspectRatio(NativeAdOptions.NATIVE_MEDIA_ASPECT_RATIO_SQUARE)
                    .setRequestMultipleImages(true)
                    .build()
            )
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }


    override fun onCleared() {
        super.onCleared()
        nativeAd?.destroy()
    }
}