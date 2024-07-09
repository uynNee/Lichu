package edu.uit.o21.lichu.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import edu.uit.o21.lichu.ui.view.mainscreen.CalendarScreen
import edu.uit.o21.lichu.ui.view.mainscreen.TodolistScreen
import edu.uit.o21.lichu.ui.view.secondaryscreen.AddCategoryScreen
import edu.uit.o21.lichu.ui.view.secondaryscreen.CategoryOnclickScreen
import edu.uit.o21.lichu.ui.view.secondaryscreen.TodoFormScreen
import edu.uit.o21.lichu.viewmodel.CategoryViewModel
import edu.uit.o21.lichu.viewmodel.ToDoViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Lichu() {
    val navController = rememberNavController()
    val todoViewModel: ToDoViewModel = viewModel()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    val shouldShowBottomBarAndFab = currentDestination in listOf("ToDoScreen", "CalendarScreen")

    Scaffold(
        bottomBar = { if (shouldShowBottomBarAndFab) Navigation(navController) },
        floatingActionButton = { if (shouldShowBottomBarAndFab) FloatingButton(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "MainGraph",
            modifier = Modifier.padding(padding)
        ) {
            navigation(startDestination = "ToDoScreen", route = "MainGraph") {
                composable("ToDoScreen") { TodolistScreen(navController) }
                composable("CalendarScreen") { CalendarScreen() }
            }
            composable("AddCategoryScreen") { AddCategoryScreen(navController) }
            composable("AddTodoScreen") { TodoFormScreen(navController) }
            composable("AddTodoWithCategoryIdScreen/{category_id}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("category_id")?.toInt()
                TodoFormScreen(navController, isAddToDo = true, categoryId = categoryId)
            }
            composable("EditTodoScreen/{todo_id}") { backStackEntry ->
                val todoId = backStackEntry.arguments?.getString("todo_id")?.toInt()
                val todo = todoId?.let { todoViewModel.todoById(it).observeAsState().value }
                if (todo != null) {
                    TodoFormScreen(navController, isEdit = true, initialTodo = todo)
                }
            }
            composable(
                route = "CategoryOnclickScreen/{category_id}/{category_name}",
                arguments = listOf(
                    navArgument("category_id") { type = NavType.IntType },
                    navArgument("category_name") { type = NavType.StringType },
                )
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                val categoryId = arguments.getInt("category_id")
                val categoryName = arguments.getString("category_name") ?: error("Missing name")

                CategoryOnclickScreen(
                    navController = navController,
                    categoryId = categoryId,
                    categoryName = categoryName
                )
            }
        }
    }
}

@Composable
fun FloatingButton(navController: NavController) {
    val categoryViewModel: CategoryViewModel = viewModel()
    val categoryList by categoryViewModel.categoryList.observeAsState(emptyList())

    var showMenu by remember { mutableStateOf(false) }
    Box {
        FloatingActionButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.clip(CircleShape),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
        DropdownMenu(
            expanded = showMenu,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondary)
                .clip(RoundedCornerShape(8.dp)),
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    navController.navigate("AddCategoryScreen")
                },
                text = {
                    Text(
                        text = "Add Category",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            )
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    if (categoryList.isEmpty()) {
                        navController.navigate("AddCategoryScreen")
                    } else
                        navController.navigate("AddTodoScreen")
                },
                text = { Text(text = "Add Todo", color = MaterialTheme.colorScheme.onSecondary) }
            )
        }
    }
}