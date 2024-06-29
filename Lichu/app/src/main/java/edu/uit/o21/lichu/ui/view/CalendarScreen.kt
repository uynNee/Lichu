package edu.uit.o21.lichu.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.uit.o21.lichu.ui.MyApp
import edu.uit.o21.lichu.ui.theme.LichuTheme
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import java.time.DayOfWeek

@Composable
fun CalendarScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        BoxWithConstraints{
            val dayHeight = (constraints.maxHeight / 15).dp
            SelectableCalendar(
                firstDayOfWeek = DayOfWeek.MONDAY,
                dayContent = { dayState -> MyDayContent(dayState, dayHeight) }
            )
        }
    }
}

@Composable
fun MyDayContent(dayState: DayState<DynamicSelectionState>, dayHeight: Dp) {
    Box(
        modifier = Modifier
            .height(dayHeight)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(
                if (!dayState.isFromCurrentMonth)
                    MaterialTheme.colorScheme.surfaceDim
                else
                    MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = if (dayState.isCurrentDay)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .padding(6.dp)
    ) {
        Text(
            text = dayState.date.dayOfMonth.toString(),
            color = if (dayState.isCurrentDay) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, fontScale = 1.15f,
//    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DefaultPreview() {
    LichuTheme {
        MyApp()
    }
}
