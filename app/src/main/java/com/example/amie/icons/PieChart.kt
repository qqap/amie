package com.example.amie.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun PieChart(
    color: Color,
    percentage: Float,
    modifier: Modifier = Modifier,
    canvasSize: Float,
) {
    val percentageState = remember { mutableStateOf(percentage) }
    LaunchedEffect(
        key1 = percentage
    ) {
        while (isActive) {
            percentageState.value = lerp(percentageState.value, percentage, 0.1f)
            delay(10)
        }
    }

    Canvas(modifier = modifier.size(canvasSize.dp)) {
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = size.minDimension / 3,
            center = Offset(size.width / 2, size.height / 2),
            style = Stroke(width = canvasSize/5)
        )
        drawArc(
            topLeft = Offset(this.size.width/4, this.size.height/4),
            color = color,
            startAngle = 270f,
            sweepAngle = percentageState.value * 360f,
            useCenter = true,
            size = this.size/2f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
    PieChart(
        color = Color.Magenta,
        percentage = 0.8f,
        canvasSize = 200f
    )
}