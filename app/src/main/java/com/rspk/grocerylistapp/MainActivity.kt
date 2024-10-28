package com.rspk.grocerylistapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE
import com.rspk.grocerylistapp.navigation.Navigation
import com.rspk.grocerylistapp.ui.theme.GroceryListAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        mobileAds()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GroceryListAppTheme { Navigation() }
        }
    }

    private fun mobileAds(){
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            val requestConfiguration = MobileAds.getRequestConfiguration().toBuilder()
                .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .build()

            MobileAds.setRequestConfiguration(requestConfiguration)

            MobileAds.initialize(this@MainActivity) {}
        }
    }
}
