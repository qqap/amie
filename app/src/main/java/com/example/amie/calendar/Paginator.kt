package com.example.amie.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amie.todayPageNumber
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Paginator(
    modifier: Modifier = Modifier,
    state: PagerState,
    jumptoPage: MutableState<Int>,
) {
    val sdf = SimpleDateFormat("EEE")
    val c: Calendar = Calendar.getInstance()
    c.add(Calendar.DATE, -1)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box (
            modifier = Modifier.padding(
                start = 18.dp,
                end = 8.dp,
                top = 6.dp,
                bottom = 6.dp
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(
                    "2024",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                )
                Text(
                    "Jan",
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        for (i in (todayPageNumber - 1) until (todayPageNumber + 6)) {
            val dayOfTheWeek: String = sdf.format(c.time).first().toString()

            val offset = state.indicatorOffsetForPage(i)
            Box(
                modifier = Modifier
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { jumptoPage.value = i },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        dayOfTheWeek,
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                end = 8.dp
                            ),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (offset > 0.1) Color(0xFFE25A6B).copy(alpha = offset) else Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "${c.get(Calendar.DAY_OF_MONTH)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (offset > 0.1) Color.White else Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (i == todayPageNumber && offset < 0.1) Color(0xFFECECEC) else
                                    Color(0xFFE25A6B).copy(alpha = offset)
                            )
                            .padding(
                                start = 8.dp,
                                end = 8.dp, bottom = 2.dp
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            c.add(Calendar.DATE, 1)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.indicatorOffsetForPage(page: Int) =  1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue
