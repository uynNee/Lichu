package edu.uit.o21.lichu.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import edu.uit.o21.lichu.ui.theme.LichuTheme

@Composable
fun Navigation(){
    var selectedItem by remember{ mutableIntStateOf(0) }
    val barItems= listOf(
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

    NavigationBar (
        modifier = Modifier.height(56.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ){
        barItems.fastForEachIndexed{ index, barItem->
            val selected = selectedItem == index
            NavigationBarItem(
                selected = selected,
                onClick = {
                    selectedItem=index
                },
                icon = {
                    Icon(
                        imageVector = barItem.icon,
                        contentDescription = barItem.title,
                        modifier = Modifier.scale(1.25f)
                    )
                },
                label = {Text(text = "", fontSize = 14.sp, lineHeight = 0.sp)},
                alwaysShowLabel = false,
                colors = NavigationBarItemColors(
                    selectedIconColor= MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor= MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedIndicatorColor= Color.Transparent,
                    unselectedIconColor= MaterialTheme.colorScheme.primary,
                    unselectedTextColor= MaterialTheme.colorScheme.primary,
                    disabledIconColor= MaterialTheme.colorScheme.outline,
                    disabledTextColor= MaterialTheme.colorScheme.outline
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
//sealed class Screens(val route : String) {
//    object Todo : Screens("home_route")
//    object Search : Screens("search_route")
//    object Profile : Screens("profile_route")
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LichuTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MyApp()
        }
    }
}