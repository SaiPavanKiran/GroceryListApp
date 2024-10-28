package com.rspk.grocerylistapp.compose.analytics

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rspk.grocerylistapp.R
import com.rspk.grocerylistapp.constants.colorsList

@Composable
fun PieChartPortrait(
    data: Map<String, Float>,
    onClick: () -> Unit = {}
) {
    BoxLayout(onClick = onClick ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PieDiagramCanvas(data = data)
            Column(
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_15)),
                verticalArrangement = Arrangement.Top
            ) {
                DiagramItemsIndicator(
                    data = data,
                    range = 0 until data.size,
                    spacerWidth = 8.dp
                )
            }
        }
    }
}

@Composable
fun PieChartLandscape(
    data: Map<String, Float>,
    onClick: () -> Unit = {}
) {
    BoxLayout(onClick = onClick) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PieDiagramCanvas(data= data)
            Column(
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.padding_22)),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = dimensionResource(id = R.dimen.padding_3)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DiagramItemsIndicator(
                        data = data,
                        range = 0 until data.size/2,
                        textModifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_7))
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DiagramItemsIndicator(
                        data = data,
                        range = data.size/2 until data.size,
                        textModifier = Modifier.padding(end =dimensionResource(id = R.dimen.padding_7))
                    )
                }
            }
        }
    }
}

@Composable
fun BoxLayout(
    onClick: () -> Unit,
    content: @Composable () -> Unit
){
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(top = dimensionResource(id = R.dimen.padding_20))
            .clip(RoundedCornerShape(9.dp))
            .background(colorResource(id = R.color.analytics_pie_chart_card_background)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_refresh_24),
                contentDescription = "refresh",
                tint = colorResource(id = R.color.analytics_pie_chart_label_text_color)
            )
        }
        content()
    }

}

@Composable
fun PieDiagramCanvas(
    data: Map<String, Float>,
) {
    val total = data.values.sum()
    val pieChartBorder = colorResource(id = R.color.analytics_pie_chart_border_color)
    val animatedSweepAngle = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedSweepAngle.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .aspectRatio(1f)
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        val radius = size.minDimension / 2
        val center = size.center
        var startAngle = -90f

        data.values.forEachIndexed { index, entry ->
            val targetSweepAngle = (entry / total) * animatedSweepAngle.value

            drawArc(
                color = pieChartBorder,
                startAngle = startAngle,
                sweepAngle = targetSweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 8f)
            )

            drawArc(
                color = colorsList[index % colorsList.size],
                startAngle = startAngle,
                sweepAngle = targetSweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius + 8, center.y - radius + 8),
                size = Size((radius - 8) * 2, (radius - 8) * 2)
            )
            startAngle += targetSweepAngle
        }
    }
}

@Composable
fun DiagramItemsIndicator(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    data: Map<String, Float>,
    range: IntRange,
    spacerWidth: Dp = 0.dp,
){
    val pieChatBorder = colorResource(id = R.color.analytics_pie_chart_border_color)
    for (i in range){
        Row(
            modifier = modifier.padding(vertical = dimensionResource(id = R.dimen.padding_4)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(modifier = Modifier.size(dimensionResource(id = R.dimen.size_12))) {
                drawCircle(color = pieChatBorder, style = Stroke(width = 5f))
                drawCircle(color = colorsList[i % colorsList.size])
            }

            Spacer(modifier = Modifier.width(spacerWidth))

            Text(
                text = data.keys.elementAt(i),
                style = TextStyle(fontSize = 16.sp),
                modifier = textModifier,
                color = colorResource(id = R.color.analytics_pie_chart_label_text_color)
            )
        }
    }
}

