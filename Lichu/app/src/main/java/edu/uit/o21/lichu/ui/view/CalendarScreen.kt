package edu.uit.o21.lichu.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import edu.uit.o21.lichu.data.entity.ToDo
import edu.uit.o21.lichu.viewmodel.ToDoViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by rememberSaveable { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    val toDoViewModel: ToDoViewModel = viewModel()
    val todos by toDoViewModel.calendarTodoList().observeAsState(emptyList())

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val todosInSelectedDate = remember(selection, todos) {
        selection?.date?.let { selectedDate ->
            todos.filter { todo ->
                val startDate = LocalDate.parse(todo.startTime.toString())
                val endDate = todo.endTime?.let { LocalDate.parse(it.toString()) } ?: startDate
                (startDate <= selectedDate) && (endDate >= selectedDate)
            }
        }.orEmpty()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
            outDateStyle = OutDateStyle.EndOfGrid,
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
        LaunchedEffect(visibleMonth) {
            selection = null
        }
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
            SimpleCalendarTitle(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                currentMonth = visibleMonth.yearMonth,
                goToPrevious = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
                },
            )
            HorizontalCalendar(
                modifier = Modifier.wrapContentWidth(),
                state = state,
                dayContent = { day ->
                    CompositionLocalProvider {
                        Day(
                            day = day,
                            isSelected = selection == day,
                            todos = todos.filter { todo ->
                                val startDate = LocalDate.parse(todo.startTime.toString())
                                val endDate = todo.endTime?.let { LocalDate.parse(it.toString()) } ?: startDate
                                (startDate <= day.date) && (endDate >= day.date)
                            }
                        ) { clicked ->
                            selection = clicked
                            openBottomSheet = true
                        }
                    }
                },
                monthHeader = {
                    MonthHeader(
                        modifier = Modifier.padding(vertical = 8.dp),
                        daysOfWeek = daysOfWeek,
                    )
                },
            )
            HorizontalDivider()
        }
    }
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            Text(
                text = "On going in: ${selection?.date}",
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items(items = todosInSelectedDate) { todo ->
                    TodoInformation(todo)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    todos: List<ToDo> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    val maxVisibleTodos = 3
    val visibleTodos = todos.take(maxVisibleTodos)
    val moreTodosCount = todos.size - visibleTodos.size
    val todoColors = listOf(Color(0xFFffd6ff), Color(0xFFc8b6ff), Color(0xFFbbd0ff))
    val dayState = remember(day) {
        mutableStateOf(day)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    )
    Box(
        modifier = Modifier
            .aspectRatio(0.575f)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            )
            .padding(1.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable(
                onClick = { onClick(day) },
            ),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = dayState.value.date.dayOfMonth.toString(),
                color = if (day.date == LocalDate.now()) MaterialTheme.colorScheme.onPrimaryContainer
                        else if (day.position == DayPosition.MonthDate) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.outline,
                fontSize = 12.sp,
                textDecoration = if (day.date == LocalDate.now()) TextDecoration.Underline else null,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                visibleTodos.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .background(color = todoColors[index % todoColors.size])
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
            Text(
                text = if (moreTodosCount > 0) "+$moreTodosCount more"
                else "",
                color = MaterialTheme.colorScheme.inverseSurface,
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

@Composable
private fun LazyItemScope.TodoInformation(todo: ToDo) {
    Row(
        modifier = Modifier
            .fillParentMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = todo.content,
            modifier = Modifier.padding(8.dp),
            fontSize = 16.sp
        )
    }
    HorizontalDivider(thickness = 2.dp)
}

operator fun LocalDate.rangeTo(other: LocalDate): List<LocalDate> {
    return generateSequence(this) { it.plusDays(1) }
        .takeWhile { it <= other }
        .toList()
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.toString(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
        )
        CalendarNavigationIcon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        imageVector = imageVector,
        contentDescription = contentDescription,
    )
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeLast()
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeFirst()
            }
            visibleItemsInfo.map { it.month }
        }
    }

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}