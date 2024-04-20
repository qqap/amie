package com.example.amie.checklist

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amie.calendar.Event
import com.example.amie.clearFocusOnKeyboardDismiss
import com.example.amie.noRippleClickable
import java.time.format.DateTimeFormatter

val ChecklistEventTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM, HH:mm")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChecklistItems(
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray,
    event: Event,
    onCheckedChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit = {},
    focusRequester: FocusRequester,
    focused: Boolean = false,
    clickableFun: () -> Unit,
    enabled: Boolean = true,
){
    val haptics = LocalHapticFeedback.current
    var itemName by remember { mutableStateOf("") }
    itemName = event.name

    var scheduledItemTime by remember { mutableStateOf("") }
    scheduledItemTime = event.start.format(ChecklistEventTimeFormatter)

    val eventMutableState = remember {
        mutableStateOf(event)
    }
    eventMutableState.value = event

    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    val focusManager = LocalFocusManager.current
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .dragAndDropSource
                (
                drawDragDecoration = {
                    drawRoundRect(
                        color = Color.Gray,
                        cornerRadius = CornerRadius(
                            x = 10.dp.toPx(),
                            y = 10.dp.toPx()
                        ),
                    )
                    drawRoundRect(
                        color = Color.White,
                        cornerRadius = CornerRadius(20f, 20f),
                        size = Size(22.dp.toPx(), 22.dp.toPx()),
                        topLeft = Offset(30f, 30f),
                        style = Stroke(width = 6f, cap = StrokeCap.Round)
                    )
                    drawText(
                        textMeasurer,
                        itemName,
                        Offset(
                            120f,
                            30f
                        ),
                        TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                }
            ) {
                detectTapGestures(
                    onLongPress = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        startTransfer(
                            DragAndDropTransferData(
                                ClipData.newPlainText(itemName, itemName),
                                localState = eventMutableState.value
                            )
                        )
                    }
                )
            }
            .padding(end = 10.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RoundedCheckbox(
            color = if (enabled) color else Color(0xFFF3F3F1),
            checkedColor = Color(0xFFe1b53e),
            onCheckedChange = onCheckedChange,
        )

        BasicTextField(
            enabled = enabled,
            modifier = Modifier
                .padding(start = 10.dp)
                .padding(2.dp)
                .focusRequester(focusRequester)
                .noRippleClickable(onClick = { clickableFun() })
                .clearFocusOnKeyboardDismiss(),
            value = itemName,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily.SansSerif,
                color = if (enabled) Color.Black else Color.LightGray,
                fontWeight = FontWeight.Medium,
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            singleLine = true,
        )
        if (event.onCalendar){
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(35))
                    .background(color = Color(0xFFf5f5f5))
                    .padding(4.dp)
            ){
                Text(
                    text = scheduledItemTime,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier,
                    textAlign = TextAlign.Right,
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        if (focused){
            focusRequester.requestFocus()
        }
    }
}