package com.example.amie.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private fun DrawScope.drawContact(
    color: Color,
) {
    drawArc(
        topLeft = Offset(0f, size.height / 1.8f),
        color = color,
        startAngle = 0f,
        sweepAngle = -180f,
        useCenter = false,
        style = Fill,
        size = Size(size.width, size.height * 0.9f)
    )
    drawArc(
        topLeft = Offset(0f, size.height * 0.8f),
        color = color,
        startAngle = 0f,
        sweepAngle = 180f,
        useCenter = false,
        style = Fill,
        size = Size(size.width, size.height / 3)
    )
}

@Composable
fun ContactIcon(
    modifier: Modifier = Modifier,
    canvasSize: Float,
    color: Color = Color.LightGray,
) {
    Box(
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .size(size = canvasSize.dp)
                .padding(canvasSize.dp / 6),
            onDraw = {
                drawCircle(
                    color = color,
                    radius = size.height / 4,
                    center = Offset(size.height / 2, size.height / 5),
                    style = Fill,
                )
                drawContact(
                    color = color,
                )
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ContactIconPreview() {
    ContactIcon(
        modifier = Modifier
            .padding(2.dp),
        color = Color.Black,
        canvasSize = 200.dp.value,
    )
}