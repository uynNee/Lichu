package edu.uit.o21.lichu.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.uit.o21.lichu.ui.theme.LichuTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp() {
    val navController = rememberNavController()
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) {
            NavHost(
                navController,
                startDestination = "Todolist",
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 38.dp)
            ) {
                composable("Todolist") {
                    // Replace with your content for Todolist screen
                    Text("Todolist Screen")
                }
                composable("Calendar") {
                    // Replace with your content for Calendar screen
                    Text("Calendar Screen")
                }
            }
        }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
    ) {
        // Define BottomNavigationItem for Todolist
        NavigationBarItem(
            modifier = Modifier.padding(top = 8.dp),
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Todolist") },
//            label = { Text("Todolist") },
            selected = currentRoute(navController) == "Todolist",
            onClick = {
                navController.navigate("Todolist") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        // Define BottomNavigationItem for Calendar
        NavigationBarItem(
            modifier = Modifier.padding(top = 10.dp),
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendar") },
//            label = { Text("Calendar") },
            selected = currentRoute(navController) == "Calendar",
            onClick = {
                navController.navigate("Calendar") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}


@Composable
private fun currentRoute(navController: NavController): String? {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    return currentBackStackEntry?.destination?.route
}
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            LichuTheme{
//                MyApp()
//            }
//        }
//    }
//}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LichuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyApp()
                }
            }
        }
    }
}
@Preview
@Composable
fun MainScreenPreview() {
    LichuTheme(darkTheme = true) {
        MyApp()
    }
}
