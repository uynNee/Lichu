package edu.uit.o21.lichu.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun Navigation(){
    var selectedItem by remember{ mutableIntStateOf(0) }
    val BarItems= listOf(
        BarItem(
            title = "ToDo",
            icon = Icons.AutoMirrored.Filled.List,
            route = "TodoScreen"
        ),
        BarItem(
            title = "Calendar",
            icon = Icons.Filled.DateRange,
            route = "CalendarScreen"
        )
    )

    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surface
    ){
        BarItems.fastForEachIndexed{ index, barItem->
            val selected= selectedItem == index
            NavigationBarItem(
                selected = selected,
                onClick = {
                    selectedItem=index
                },
                icon = {
                    Icon(
                        imageVector = barItem.icon,
                        contentDescription = barItem.title
                    )
                },
                label = { Text(text = barItem.title)},
                alwaysShowLabel = selected,
                colors = NavigationBarItemColors(
                    selectedIconColor= MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor= MaterialTheme.colorScheme.onSecondary,
                    selectedIndicatorColor= Color.Transparent,
                    unselectedIconColor= MaterialTheme.colorScheme.secondary,
                    unselectedTextColor= MaterialTheme.colorScheme.secondary,
                    disabledIconColor=Color.White,
                    disabledTextColor=Color.White
                )
            )
        }
    }
}
data class BarItem(
    val title: String,
    val icon: ImageVector,
    val route:String
)