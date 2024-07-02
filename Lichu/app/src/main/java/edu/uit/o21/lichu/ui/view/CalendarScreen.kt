package edu.uit.o21.lichu.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen() {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    val (firstVisibleMonth, setFirstVisibleMonth) = remember { mutableStateOf(currentMonth) }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = firstVisibleMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp, start = 12.dp, end = 6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Events",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            )
            Box(
                modifier = Modifier
                    .clickable {
                        selectedDate = LocalDate.now()
                        currentMonth = YearMonth.now()
                        setFirstVisibleMonth(currentMonth)
                        coroutineScope.launch {
                            state.scrollToMonth(currentMonth)
                        }
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .size(34.dp)
            ) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    Text(
                        text = LocalDate.now().dayOfMonth.toString(),
                        style = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text(
                        text = getOrdinalSuffix(LocalDate.now().dayOfMonth),
                        style = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                currentMonth = currentMonth.minusMonths(1)
                setFirstVisibleMonth(currentMonth)
                coroutineScope.launch {
                    state.scrollToMonth(currentMonth)
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
            }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                DayOfMonthTitle(selectedDate = selectedDate)
            }
            IconButton(onClick = {
                currentMonth = currentMonth.plusMonths(1)
                setFirstVisibleMonth(currentMonth)
                coroutineScope.launch {
                    state.scrollToMonth(currentMonth)
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
            }
        }
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day, isSelected = selectedDate == day.date, currentMonth = currentMonth) { clickedDay ->
                    selectedDate = clickedDay.date
                }
            },
            monthHeader = {
                MonthHeader(daysOfWeek = daysOfWeek)
            }
        )
    }
}

fun getOrdinalSuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

@Composable
fun Day(day: CalendarDay, isSelected: Boolean, currentMonth: YearMonth, onClick: (CalendarDay) -> Unit) {
    val isToday = day.date == LocalDate.now()
    val isCurrentMonth = day.date.yearMonth == currentMonth
    val textColor = if (isCurrentMonth) {
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        Color.Gray
    }
    Box(
        modifier = Modifier
            .aspectRatio(0.525f)
            .background(
                color =
                if (isSelected) MaterialTheme.colorScheme.surfaceContainer
                else Color.Transparent
            )
            .clickable(
                onClick = { onClick(day) }
            )
            .then(if (isToday) Modifier.border(1.dp, MaterialTheme.colorScheme.primary)
            else Modifier),
        contentAlignment = Alignment.TopCenter
    ) {
        Spacer(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.primary),
        )
        Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            style =
            if (isToday) MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
            else MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DayOfMonthTitle(selectedDate: LocalDate) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "${selectedDate.dayOfMonth} " +
                    "${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} " +
                    "${selectedDate.year}",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
