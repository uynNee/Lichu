package edu.uit.o21.lichu.ui

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController

@Composable
fun Navigation(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val primaryColor = MaterialTheme.colorScheme.primary
    val barItems = listOf(
        BarItem(
            title = "ToDo",
            icon = Icons.AutoMirrored.Filled.List,
            route = "ToDoScreen"
        ),
        BarItem(
            title = "Calendar",
            icon = Icons.Filled.DateRange,
            route = "CalendarScreen"
        )
    )
    NavigationBar(
        modifier = Modifier.height(56.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        barItems.fastForEachIndexed { index, barItem ->
            val selected = selectedItem == index
            NavigationBarItem(
                selected = selected,
                onClick = {
                    selectedItem = index
                    navController.navigate(barItem.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = barItem.icon,
                        contentDescription = barItem.title,
                        modifier = Modifier.scale(1.25f)
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedIndicatorColor = Color.Transparent,
                    unselectedIconColor = primaryColor,
                    unselectedTextColor = primaryColor,
                    disabledIconColor = MaterialTheme.colorScheme.outline,
                    disabledTextColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .then(
                        if (selected) {
                            Modifier.drawBehind {
                                val strokeWidth = 4.dp.toPx()
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, 0f),
                                    strokeWidth = strokeWidth
                                )
                            }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}

data class BarItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)