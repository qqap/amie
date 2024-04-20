package com.example.amie.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SplitViewDragIcon(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Float = 48.dp.value,
) {
    Canvas(
        modifier = modifier.size(size = size.dp),
        onDraw = {
            drawRoundedRectangle(
                color = color,
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun SplitViewDragIconPreview() {
    Canvas(
        modifier = Modifier.size(size = 48.dp),
        onDraw = {
            drawRoundedRectangle(
                color = Color.Black,
            )
        }
    )
}

private fun DrawScope.drawRoundedRectangle(
    color: Color,
) {
    drawRoundRect(
        topLeft = Offset(0f, size.height/2 - size.height/12),
        color = color,
        cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
        style = Fill,
        size = Size(size.width, size.height/6)
    )
}