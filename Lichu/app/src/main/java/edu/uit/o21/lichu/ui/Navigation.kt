package edu.uit.o21.lichu.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.uit.o21.lichu.ui.view.TodolistScreen

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
            modifier = Modifier.padding(bottom = 58.dp)
        ) {
            composable("Todolist") {
                // Replace with your content for Todolist screen
                TodolistScreen()
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
    val selectedColor = Color(0xFFDF8CA4)
    val unselectedColor = Color.White
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
    ) {
        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = unselectedColor,
                selectedTextColor = selectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.padding(top = 8.dp),
            icon = {
                    Icon(Icons.AutoMirrored.Filled.List,
                        contentDescription = "Todolist"
                    )
            },
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
        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                unselectedIconColor = unselectedColor,
                selectedTextColor = selectedColor,
                unselectedTextColor = unselectedColor,
                indicatorColor = Color.Transparent
            ),
            modifier = Modifier.padding(top = 10.dp),
            icon = {
                        Icon(Icons.Filled.DateRange,
                            contentDescription = "Calendar"
                        )
                   },
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