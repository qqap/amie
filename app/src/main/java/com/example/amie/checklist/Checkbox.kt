package com.example.amie.checklist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedCheckSymbol(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    size: Float = 48.dp.value,
) {
    Canvas(
        modifier = modifier.size(size = size.dp),
        onDraw = {
            drawRoundedCheck(
                color = color,
            )
        }
    )
}

private fun DrawScope.drawRoundedCheck(
    color: Color,
) {
    val path = Path().apply {
        moveTo(size.width * 0.25f, size.height * 0.55f)
        lineTo(size.width * 0.4f, size.height * 0.7f)
        lineTo(size.width * 0.75f, size.height * 0.3f)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = size.width * 0.15f,
            join = StrokeJoin.Round,
            cap = StrokeCap.Round
        )
    )
}


@Preview(showBackground = true)
@Composable
fun RoundedCheckSymbolPreview() {
    RoundedCheckSymbol(
        modifier = Modifier
            .padding(2.dp),
        color = Color.Black,
    )
}

@Preview(showBackground = true)
@Composable
fun RoundedCheckboxPreview(){
    RoundedCheckbox(
        modifier = Modifier.padding(16.dp),
        color = Color(0xFF0e5782),
        checkedColor = Color(0xFF0e5782),
        onCheckedChange = null,
    )
}

@Composable
fun RoundedCheckbox(
    modifier: Modifier = Modifier,
    color: Color,
    checkedColor: Color = color,
    onCheckedChange: ((Boolean) -> Unit)?,
){

    val isChecked = remember { mutableStateOf(false) }
    val colorState = remember { mutableStateOf(Color.Transparent) }
    val borderState = remember { mutableStateOf(color) }

    Box(
        modifier = modifier
            .size(22.dp)
            .clip(RoundedCornerShape(35))
            .background(color = colorState.value)
            .border(
                width = 2.dp,
                color = borderState.value,
                shape = RoundedCornerShape(35)
            )
            .toggleable(value = isChecked.value, role = Role.Checkbox) {
                isChecked.value = it
                if (onCheckedChange != null) {
                    onCheckedChange(isChecked.value)
                }
                if (isChecked.value) {
                    colorState.value = checkedColor
                    borderState.value = checkedColor
                } else {
                    colorState.value = Color.Transparent
                    borderState.value = color
                }
            }
    ){
        if (isChecked.value) {
            // rounded draw icon
            RoundedCheckSymbol(
                modifier = Modifier
                    .padding(2.dp),
                color = Color.White,
            )
        }
    }
}