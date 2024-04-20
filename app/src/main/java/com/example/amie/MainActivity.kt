package com.example.amie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.util.lerp
import com.example.amie.calendar.CalendarEvent
import com.example.amie.calendar.Event
import com.example.amie.calendar.Paginator
import com.example.amie.calendar.sampleEvents
import com.example.amie.checklist.ChecklistItems
import com.example.amie.checklist.ChecklistTitle
import com.example.amie.icons.ContactIcon
import com.example.amie.icons.SearchIcon
import com.example.amie.icons.SplitViewDragIcon
import com.example.amie.ui.theme.AmieTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.Calendar
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AmieTheme {
                window.statusBarColor = Color.White.toArgb()
                LazyColumn(
                    Modifier.imePadding()
                ) {
                    item {
                        Spacer(
                            Modifier.windowInsetsTopHeight(
                                WindowInsets.systemBars
                            )
                        )
                        Screen()
                    }
                }
            }
        }
    }
}

const val todayPageNumber = 365 * 100 // 100th year
const val totalPageNumbers = 365 * 100 * 2 // 200 years

val roundedCornerRadius = 25.dp
val dividerColor = Color.hsv(0f, 0f, 0.92f)

const val numHours = 24

val hourHeight = 80.dp
val pagerHeight = 52.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Screen(modifier: Modifier = Modifier) {
    val textMeasurer: TextMeasurer = rememberTextMeasurer()
    val measuredText: TextLayoutResult = textMeasurer.measure(
        text = "####00:00##",
        TextStyle(
            fontSize = 12.sp,
        )
    )

    val minLocalTime: LocalTime = LocalTime.MIN

    val eventsMap: SnapshotStateList<Event> = remember {
        sampleEvents.toMutableStateList()
    }

    var dragBoxPosition: Int by remember { mutableIntStateOf(0) }
    var dragBoxSize by remember { mutableFloatStateOf(0.25f) }
    var dragBoxEnabled by remember { mutableStateOf(false) }

    val scrollState: ScrollState = rememberScrollState()
    val autoScrollSpeed = remember { mutableStateOf(0f) }
    val scrollTo = remember { mutableFloatStateOf(0f) }

    val pagerState = rememberPagerState(
        pageCount = { totalPageNumbers },
        initialPage = todayPageNumber,
    )
    val pagerSpeed = remember { mutableStateOf(0) }
    val jumpToPage = remember { mutableIntStateOf(0) }


    LaunchedEffect(pagerSpeed.value, jumpToPage.intValue) {
        if (pagerSpeed.value != 0) {
            while (isActive) {
                pagerState.animateScrollToPage(
                    page = if (pagerSpeed.value == 0) pagerState.currentPage else pagerState.currentPage + pagerSpeed.value,
                    animationSpec = tween(
                        500
                    ),
                )
                delay(250)
            }
        }
        if (jumpToPage.intValue != 0) {
            pagerState.animateScrollToPage(
                page = jumpToPage.intValue,
                animationSpec = tween(750)
            )
            jumpToPage.intValue = 0
        }
    }

    LaunchedEffect(autoScrollSpeed.value, scrollTo.floatValue) {
        if (autoScrollSpeed.value != 0f) {
            while (isActive) {
                scrollState.scrollBy(autoScrollSpeed.value)
                delay(10)
            }
        }
        if (scrollTo.floatValue != 0f) {
            while (isActive) {
                scrollState.scrollTo(
                    lerp(
                        scrollState.value,
                        scrollState.value - scrollTo.floatValue.toInt(),
                        2f
                    )
                )
                delay(10)
            }
            scrollTo.floatValue = 0f
        }
    }

    var calendarHeight by remember { mutableStateOf(1200f) }
    val currentHour = remember { mutableStateOf(0) }
    val currentMinutes = remember { mutableStateOf(0) }

    LaunchedEffect(0) {
        while (true) {
            currentHour.value = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            currentMinutes.value = Calendar.getInstance().get(Calendar.MINUTE)
            delay(1000)
        }
    }

    val configuration = LocalConfiguration.current
    val screenHeight =
        configuration.screenHeightDp.dp + WindowInsets.navigationBars.asPaddingValues()
            .calculateBottomPadding()

    Column(
        modifier = modifier.background(color = Color.Black),
    ) {
        Paginator(
            modifier = Modifier
                .height(pagerHeight)
                .fillMaxWidth()
                .background(color = Color.White),
            state = pagerState,
            jumptoPage = jumpToPage,
        )
        Box(
            modifier = Modifier
                .height(with(LocalDensity.current) { calendarHeight.toDp() - pagerHeight })
                .clip(
                    RoundedCornerShape(
                        bottomStart = roundedCornerRadius,
                        bottomEnd = roundedCornerRadius
                    )
                )
                .verticalFadingEdge(
                    scrollState = scrollState,
                    length = 20.dp,
                    edgeColor = Color.White
                )
                .background(color = Color.White)
                .verticalScroll(scrollState)
                .padding(top = 10.dp)

        ) {
            Column {
                HorizontalPager(state = pagerState) { page ->
                    val currentDate = LocalDate.now().plusDays((page - todayPageNumber).toLong())
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(numHours * hourHeight)
                            .background(
                                color = Color.White
                            )
                            .drawWithContent {
                                val firstHour = minLocalTime.truncatedTo(ChronoUnit.HOURS)
                                val firstHourOffsetMinutes =
                                    if (firstHour == minLocalTime) 0 else ChronoUnit.MINUTES.between(
                                        minLocalTime,
                                        firstHour.plusHours(1)
                                    )
                                val firstHourOffset =
                                    (firstHourOffsetMinutes / 60f) * hourHeight.toPx()
                                repeat(numHours) {
                                    drawText(
                                        textMeasurer,
                                        "    ${String.format("%02d", it)}:00  ",
                                        Offset(
                                            0f,
                                            0f + it * hourHeight.toPx() - firstHourOffset - measuredText.size.height.toFloat() / 2
                                        ),
                                        TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            color = Color.hsv(0f, 0f, 0.60f),
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    )
                                    drawLine(
                                        dividerColor,
                                        start = Offset(
                                            measuredText.size.width.toFloat(),
                                            it * hourHeight.toPx()
                                        ),
                                        end = Offset(
                                            measuredText.size.width.toFloat() + size.width,
                                            it * hourHeight.toPx()
                                        ),
                                        strokeWidth = 1.5.dp.toPx()
                                    )
                                }

                                if (page == todayPageNumber){
                                    drawLine(
                                        Color.Red,
                                        start = Offset(
                                            measuredText.size.width.toFloat(),
                                            currentHour.value * hourHeight.toPx() + currentMinutes.value * hourHeight.toPx() / 60
                                        ),
                                        end = Offset(
                                            measuredText.size.width.toFloat() + size.width,
                                            currentHour.value * hourHeight.toPx() + currentMinutes.value * hourHeight.toPx() / 60
                                        ),
                                        strokeWidth = 1.5.dp.toPx()
                                    )
                                }

                                drawContent()

                                if (dragBoxEnabled) {

                                    drawRoundRect(
                                        color = Color.Black,
                                        cornerRadius = CornerRadius(
                                            x = 8.dp.toPx(),
                                            y = 8.dp.toPx()
                                        ),
                                        style = Stroke(width = 2.dp.toPx()),
                                        topLeft = Offset(
                                            160f,
                                            dragBoxPosition * hourHeight.toPx() / 4 + 2.dp.toPx()
                                        ),
                                        size = Size(
                                            340.dp.toPx(),
                                            hourHeight.toPx() * dragBoxSize - 4.dp.toPx()
                                        ),
                                    )

                                    val drawBoxEnd = dragBoxPosition + (dragBoxSize * 4).toInt()
                                    val startTimeHour = String.format("%02d", dragBoxPosition / 4)
                                    val startTimeMinutes = String.format("%02d", (dragBoxPosition % 4) * 15)
                                    val endTimeHour = String.format("%02d", (drawBoxEnd) / 4)
                                    val endTimeMinutes = String.format("%02d", ((drawBoxEnd) % 4) * 15)

                                    drawText(
                                        textMeasurer,
                                        "    ${startTimeHour}:${startTimeMinutes}  ",
                                        Offset(
                                            0f,
                                            (0f + ((dragBoxPosition * hourHeight.toPx()) / 4)) - firstHourOffset - (measuredText.size.height.toFloat() / 2)
                                        ),
                                        TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            color = Color.Black,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    )

                                    drawText(
                                        textMeasurer,
                                        "    ${endTimeHour}:${endTimeMinutes}  ",
                                        Offset(
                                            0f,
                                            (0f + (drawBoxEnd * hourHeight.toPx() / 4)) - firstHourOffset - (measuredText.size.height.toFloat() / 2)
                                        ),
                                        TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            color = Color.Black,
                                            fontWeight = FontWeight.SemiBold,
                                        )
                                    )
                                }
                            }

                    ) {

                        Layout(
                            content = {
                                eventsMap.sortedBy(Event::start).forEach { event ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(numHours * hourHeight)
                                            .padding(start = 60.dp, top = 2.dp)
                                            .eventData(event)
                                    ) {
                                        CalendarEvent(event = event)
                                    }
                                }
                            },

                            ) { measureables, constraints ->
                            val height = hourHeight.roundToPx() * 24
                            val dayWidth = 405.dp
                            val width = dayWidth.roundToPx()
                            val placeablesWithEvents = measureables.map { measurable ->
                                val event = measurable.parentData as Event
                                val eventDurationMinutes =
                                    ChronoUnit.MINUTES.between(event.start, event.end)
                                val eventHeight =
                                    ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        minWidth = dayWidth.roundToPx(),
                                        maxWidth = dayWidth.roundToPx(),
                                        minHeight = eventHeight,
                                        maxHeight = eventHeight
                                    )
                                )
                                Pair(placeable, event)
                            }
                            layout(width, height) {
                                placeablesWithEvents.forEach { (placeable, event) ->
                                    val eventOffsetMinutes = ChronoUnit.MINUTES.between(
                                        LocalTime.MIN,
                                        event.start.toLocalTime()
                                    )
                                    val eventY =
                                        ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                                    val eventOffsetDays =
                                        ChronoUnit.DAYS.between(currentDate, event.start.toLocalDate())
                                            .toInt()
                                    val eventX = eventOffsetDays * dayWidth.roundToPx()
                                    placeable.place(eventX, eventY)
                                }
                            }
                        }

                    }
                }
            }

            Column {
                repeat(numHours * 4) {
                    Box(
                        modifier
                            .fillMaxWidth()
                            .height(hourHeight / 4)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event: DragAndDropEvent ->
                                    return@dragAndDropTarget true

                                },
                                target = DragAndDropTarget(
                                    onMoved = { event: DragAndDropEvent ->
                                        dragBoxEnabled = true
                                        var eventDurationMinutes = 60f
                                        if (event.toAndroidDragEvent().localState != null) {
                                            val dropEvent: Event =
                                                event.toAndroidDragEvent().localState as Event
                                            eventDurationMinutes = ChronoUnit.MINUTES
                                                .between(dropEvent.start, dropEvent.end)
                                                .toFloat()
                                        }

                                        if (event.toAndroidDragEvent().y > calendarHeight + 100 && event.toAndroidDragEvent().y < calendarHeight + 200) {
                                            autoScrollSpeed.value = 15f
                                        } else if (event.toAndroidDragEvent().y < 300) {
                                            autoScrollSpeed.value = -15f
                                        } else if (event.toAndroidDragEvent().x > 900 && event.toAndroidDragEvent().y < calendarHeight + 100) {
                                            pagerSpeed.value = 1
                                        } else if (event.toAndroidDragEvent().x < 100 && event.toAndroidDragEvent().y < calendarHeight + 100) {
                                            pagerSpeed.value = -1
                                        } else {
                                            autoScrollSpeed.value = 0f
                                            pagerSpeed.value = 0
                                            dragBoxPosition = it
                                            dragBoxSize = (eventDurationMinutes / 60f)
                                        }
                                    },

                                    onDrop = { event: DragAndDropEvent ->
                                        autoScrollSpeed.value = 0f
                                        pagerSpeed.value = 0
                                        if (event.toAndroidDragEvent().localState != null) {

                                            val dropEvent: Event =
                                                event.toAndroidDragEvent().localState as Event

                                            val eventDuration = ChronoUnit.MINUTES
                                                .between(dropEvent.start, dropEvent.end)

                                            val dayOfYear = LocalDate
                                                .now()
                                                .plusDays((pagerState.currentPage - todayPageNumber).toLong()).dayOfYear

                                            dragBoxEnabled = false

                                            eventsMap[eventsMap.indexOf(dropEvent)] =
                                                dropEvent.copy(
                                                    start = dropEvent.start
                                                        .withDayOfYear(dayOfYear)
                                                        .withHour((it / 4))
                                                        .withMinute((it % 4) * 15),
                                                    end = dropEvent.end
                                                        .withDayOfYear(dayOfYear)
                                                        .withHour((it / 4))
                                                        .withMinute((it % 4) * 15)
                                                        .plusMinutes(
                                                            eventDuration
                                                        ),
                                                    onCalendar = true
                                                )
                                        }
                                        return@DragAndDropTarget true
                                    }
                                )
                            )
                    )
                }
            }
        }
        val text = remember { mutableStateOf("hello") }
        Box(
            modifier = Modifier.height(30.dp)
        ) {
            Row {
                Spacer(modifier = Modifier.weight(0.2f))
                ContactIcon(
                    modifier.padding(4.dp),
                    canvasSize = 20.dp.value,
                    color = Color(0xFFa7a7a7)
                )
                Spacer(Modifier.weight(1f))

                SplitViewDragIcon(
                    modifier
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                calendarHeight += dragAmount.y
                                scrollTo.floatValue = dragAmount.y / 4f
                                text.value = dragAmount.y.toString()
                            }
                        },
                    size = 48.dp.value,
                    color = Color(0xFFd7d7d7)
                )
                Spacer(Modifier.weight(1f))

                SearchIcon(
                    canvasSize = 24.dp.value,
                    color = Color(0xFFa7a7a7)
                )
                Spacer(modifier = Modifier.weight(0.2f))
            }
        }

        Box(
            modifier = Modifier
                .height(
                    with(LocalDensity.current) {
                        screenHeight - calendarHeight.toDp() - 30.dp
                    }
                )
                .clip(
                    RoundedCornerShape(
                        topStart = roundedCornerRadius,
                        topEnd = roundedCornerRadius,
                    )
                )
                .fillMaxWidth()
                .background(color = Color.White)

        ) {
            val scrollableState: ScrollState = rememberScrollState()
            var percentage by remember { mutableFloatStateOf(0.4f) }
            percentage = (eventsMap.count { it.checked and it.checkboxItem }
                .toFloat() / eventsMap.count { it.checkboxItem }.toFloat())

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                ChecklistTitle(
                    percentage = percentage,
                    totalNumber = eventsMap.count { it.checkboxItem }
                )
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollableState)
                        .padding(start = 30.dp, end = 20.dp, top = 10.dp)
                ) {
                    val focusRequester = remember { FocusRequester() }
                    for (event in eventsMap) {
                        if (event.checkboxItem) {
                            val focused = event.name == ""

                            ChecklistItems(event = event, onCheckedChange = {
                                eventsMap[eventsMap.indexOf(event)] = event.copy(
                                    checked = it
                                )
                            }, onValueChange = {
                                eventsMap[eventsMap.indexOf(event)] = event.copy(
                                    name = it
                                )
                            }, focusRequester = focusRequester, focused = focused,
                                clickableFun = {})
                        }
                    }

                    ChecklistItems(
                        event = Event(
                            name = "todo",
                            color = Color(0xFFe1b53e),
                            start = LocalDateTime.now(),
                            end = LocalDateTime.now() + Duration.ofHours(1),
                            checkboxItem = true,
                            checked = false,
                        ),
                        onCheckedChange = {},
                        onValueChange = {},
                        focusRequester = focusRequester,
                        clickableFun = {
                            eventsMap.add(
                                Event(
                                    name = "",
                                    color = Color(0xFFe1b53e),
                                    start = LocalDateTime.now(),
                                    end = LocalDateTime.now() + Duration.ofHours(1),
                                    checkboxItem = true,
                                    checked = false,
                                )
                            )
                            calendarHeight -= 60f
                            focusRequester.requestFocus()

                        },
                        enabled = false
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    Screen()
}
