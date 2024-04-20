package com.example.amie.calendar

import android.content.ClipData
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Duration
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarEvent(
    event: Event,
    modifier: Modifier = Modifier,
) {
    val internalEventState = remember { mutableStateOf(event) }
    internalEventState.value = event

    val haptics = LocalHapticFeedback.current
    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    val lightness = 2f
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                end = 2.dp,
                bottom = 2.dp
            )
            .clipToBounds()
            .background(
                event.color.copy(
                    red = Math.min(event.color.red * lightness, 1f),
                    green = Math.min(event.color.green * lightness, 1f),
                    blue = Math.min(event.color.blue * lightness, 1f)
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp)
            .dragAndDropSource
                (
                drawDragDecoration = {
                    drawRoundRect(
                        color = event.color,
                        cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx()),
                    )
                    drawText(
                        textMeasurer,
                        event.name,
                        Offset(
                            40f,
                            30f
                        ),
                        TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                        )
                    )
                },
            )
            {
                detectTapGestures(
                    onLongPress = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        startTransfer(
                            DragAndDropTransferData(
                                ClipData.newPlainText(event.name, event.name),
                                localState = internalEventState.value
                            )
                        )
                    }
                )
            }

    ) {
        val hours = Duration.between(event.start, event.end).toHours()
        Row(
            Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.name,
                    color = event.color,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (event.checked) TextDecoration.LineThrough else TextDecoration.None,
                )
            }
            val minutes = Duration.between(event.start, event.end).toMinutes() % 60
            Text(
                text = "${if (hours > 0 ) hours.toString() + "h" else ""}  ${if (minutes > 0) minutes.toString() + "m" else ""}",
                style = MaterialTheme.typography.labelMedium,
                color = event.color.copy(alpha = 0.6f),
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Right,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarEventPreview() {
    CalendarEvent(
        event = Event(
            name = "Event Name",
            start = LocalDateTime.now(),
            end = LocalDateTime.now() + Duration.ofHours(1),
            color = Color(0xFF0e5782),
        ),
        modifier = Modifier
    )
}

@Preview(showBackground = true)
@Composable
fun CalendarEventCheckedPreview() {
    CalendarEvent(
        event = Event(
            name = "Event Name",
            start = LocalDateTime.now(),
            end = LocalDateTime.now() + Duration.ofHours(1),
            color = Color(0xFF0e5782),
            checked = true
        ),
        modifier = Modifier
    )
}