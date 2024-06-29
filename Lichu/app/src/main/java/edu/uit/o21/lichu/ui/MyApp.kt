package edu.uit.o21.lichu.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.uit.o21.lichu.ui.theme.LichuTheme
import edu.uit.o21.lichu.ui.view.CalendarScreen
import edu.uit.o21.lichu.ui.view.TodolistScreen
import edu.uit.o21.lichu.viewmodel.CategoryViewModel
import io.github.boguszpawlowski.composecalendar.day.DefaultDay

@Composable
fun MyApp() {
    val categoryViewModel: CategoryViewModel = viewModel()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { Navigation(navController) },
        floatingActionButton = {FloatingButton(categoryViewModel)}
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "ToDoScreen",
            modifier = Modifier.padding(padding)
        ) {
            composable("ToDoScreen") { TodolistScreen() }
            composable("CalendarScreen") { }
        }
    }
}
@Composable
fun FloatingButton(categoryViewModel: CategoryViewModel){
    var showMenu by remember { mutableStateOf(false) }
    var showAddListDialog by remember { mutableStateOf(false) }
    var showAddTodoDialog by remember { mutableStateOf(false) }
    Box {
        FloatingActionButton(
            onClick = { showMenu = !showMenu },
            modifier = Modifier.clip(RoundedCornerShape(36.dp)),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
        DropdownMenu(
            expanded = showMenu,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clip(RoundedCornerShape(8.dp)),
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {showMenu = false
                    showAddListDialog = true
                },
                text = { Text(text = "Add List", color = MaterialTheme.colorScheme.onTertiaryContainer)}
            )
            DropdownMenuItem(
                onClick = { showMenu = false
                    showAddTodoDialog= true
                },
                text = { Text(text = "Add Todo", color = MaterialTheme.colorScheme.onTertiaryContainer)}
            )
        }
    }
    if (showAddListDialog) {
        AddListDialog(categoryViewModel,onDismiss = { showAddListDialog = false })
    }

    if (showAddTodoDialog) {
        AddTodoDialog(onDismiss = { showAddTodoDialog = false})
    }
}

@Composable
fun AddTodoDialog(onDismiss: () -> Unit){

}

@Composable
fun AddListDialog(categoryViewModel:CategoryViewModel,onDismiss: () -> Unit) {
    var textState by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add New List")
        },
        text = {
            Column {
                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("List Name") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    categoryViewModel.addCategory(textState)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    LichuTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//        MyApp()
//        }
//    }
//}