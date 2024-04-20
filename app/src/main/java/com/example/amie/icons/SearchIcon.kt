package com.example.amie.icons

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchIcon(
    modifier: Modifier = Modifier,
    color: Color,
    canvasSize: Float,
) {
    Box(
        modifier = modifier.padding(
            start = canvasSize.dp / 5,
            top = canvasSize.dp / 5,
        )
    ){
        Canvas(
            modifier = Modifier.size(size = canvasSize.dp),
            onDraw = {
                drawSearchIcon(
                    color = color,
                )
            }
        )
    }
}

private fun DrawScope.drawSearchIcon(
    color: Color,
) {
    val path = Path().apply {
        moveTo(size.width * 0.5f, size.height * 0.55f)
        lineTo(size.width * 0.625f, size.height * 0.675f)
    }

    drawCircle(
        color = color,
        radius = size.height / 4,
        center = Offset(size.width / 3, size.height / 3),
        style = Stroke(width = size.width * 0.075f)
    )

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = size.width * 0.125f,
            cap = StrokeCap.Round
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchIconPreview() {
    SearchIcon(
        modifier = Modifier
            .padding(2.dp),
        color = Color.Black,
        canvasSize = 200.dp.value,
    )
}