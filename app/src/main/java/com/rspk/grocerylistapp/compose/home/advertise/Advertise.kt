package com.rspk.grocerylistapp.compose.home.advertise

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.common.composables.LoadingScreen

@Composable
fun Advertise(
    modifier: Modifier = Modifier,
    advertiseViewModel: AdvertiseViewModel = hiltViewModel()
) {
    if(!advertiseViewModel.loading){
        advertiseViewModel.nativeAd?.let {
            NativeAdView(ad = it) { ad, view ->
                LoadAdContent(ad, view)
            }
        }
    }
}

@Composable
fun NativeAdView(
    ad: NativeAd,
    adContent: @Composable (ad: NativeAd, contentView: View) -> Unit
) {
    val contentViewId by remember { mutableIntStateOf(View.generateViewId()) }
    val adViewId by remember { mutableIntStateOf(View.generateViewId()) }
    AndroidView(
        factory = { context ->
            val contentView = ComposeView(context).apply {
                id = contentViewId
            }
            NativeAdView(context).apply {
                id = adViewId
                addView(contentView)
            }
        },
        update = { view ->
            val adView = view.findViewById<NativeAdView>(adViewId)
            val contentView = view.findViewById<ComposeView>(contentViewId)

            adView.setNativeAd(ad)
            adView.callToActionView = contentView
            contentView.setContent { adContent(ad, contentView) }
        }
    )
}

@Composable
private fun LoadAdContent(
    nativeAd: NativeAd?,
    composeView: View,
    configuration: Configuration = LocalConfiguration.current,
) {
    nativeAd?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(configuration.screenWidthDp.dp)
                .background(color = colorResource(id = R.color.ad_box_color))
                .clickable {
                    composeView.performClick()
                },
        ) {
            Text(
                text = "Ad",
                modifier = Modifier
                    .weight(0.15f)
                    .wrapContentSize(Alignment.TopStart)
                    .background(Color.White),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Image(
                painter = rememberAsyncImagePainter(model = it.images[0].uri),
                contentDescription = "Advertisement",
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentScale = ContentScale.Fit,
            )
            Row(
                modifier = Modifier.weight(0.3f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(text = it.headline?: "",
                    fontSize = 15.sp,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_10)),
                    color = colorResource(id = R.color.ad_box_text_color),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                it.icon?.let {
                    Image(
                        painter = rememberAsyncImagePainter(model = it.uri),
                        contentDescription = "Advertisement",
                        modifier = Modifier
                            .size(dimensionResource(id = R.dimen.size_35))
                            .clip(RoundedCornerShape(10.dp))
                    )
                }
            }
            Text(
                text = it.body?: "",
                fontSize = 15.sp,
                modifier = Modifier
                    .weight(0.3f)
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                color = colorResource(id = R.color.ad_box_text_color),
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
