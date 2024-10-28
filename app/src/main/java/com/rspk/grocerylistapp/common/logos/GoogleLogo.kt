package com.rspk.grocerylistapp.common.logos

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.R.drawable as AppIcon

@Composable
fun GoogleLogo(
    text:String,
    enabled:Boolean = true,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_5))
    ){
        Icon(
            painter = painterResource(id = AppIcon.google_logo_search_new_svgrepo_com),
            contentDescription = "Google Logo",
            modifier = Modifier.size(dimensionResource(id = R.dimen.size_18)),
            tint = if(enabled) Color.Unspecified else Color.LightGray
        )
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = if(enabled) colorResource(id = R.color.google_button_text_color) else Color.LightGray
            )
        )
    }
}