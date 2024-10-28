package com.rspk.grocerylistapp.common.modifier

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.R.color as AppColor

@Composable
fun Modifier.signingNavigationModifier(
    configuration: Configuration = LocalConfiguration.current
) =
    this.let {
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            it.fillMaxHeight()
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .width(configuration.screenWidthDp.dp/2)
                .verticalScroll(rememberScrollState())
        }else{
            it.fillMaxSize()
        }
    }

@Composable
fun Modifier.linearProgressModifier()=
    this.padding(horizontal = dimensionResource(id = R.dimen.padding_10))
        .padding(bottom = dimensionResource(id = R.dimen.padding_10))
        .fillMaxWidth()
        .height(dimensionResource(id = R.dimen.size_30))
        .border(
            border = BorderStroke(1.dp, colorResource(id = R.color.progress_bar_border_color)),
            shape = RoundedCornerShape(20.dp)
        )