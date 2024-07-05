package edu.uit.o21.lichu.ui.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.uit.o21.lichu.data.entity.ToDo
import edu.uit.o21.lichu.viewmodel.CategoryViewModel
import edu.uit.o21.lichu.viewmodel.ToDoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(navController: NavController) {
    val categoryViewModel: CategoryViewModel = viewModel()
    val todoViewModel: ToDoViewModel = viewModel()

    var content by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    val currentDate = LocalDate.now()

    var expanded by remember { mutableStateOf(false) }
    val categoryListState = categoryViewModel.categoryList.observeAsState(emptyList())
    val categoryList = categoryListState.value
    var selectedCategory by remember(categoryList) {
        mutableStateOf(if (categoryList.isNotEmpty()) categoryList[0] else null)
    }
    LaunchedEffect(categoryListState) {
        if (categoryList.isNotEmpty()) {
            selectedCategory = categoryList[0]
        }
    }
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var isContentEmpty by remember { mutableStateOf(content.isEmpty()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create a new todo",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = content,
                onValueChange = {
                    content = it
                    isContentEmpty = it.isEmpty()
                },
                label = { Text("Content") },
                singleLine=true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerField(
                label = "End Date",
                selectedDate = endDate,
                onDateSelected = {
                    endDate = it
                },
                dateFormatter = dateFormatter
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                selectedCategory?.let {
                    TextField(
                        value = it.name,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Select Sample") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                }
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categoryList.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (content.isNotEmpty()) {
                            selectedCategory?.let { category ->
                                val todo = ToDo(
                                    content = content,
                                    startTime = currentDate,
                                    endTime = endDate,
                                    isDone = false,
                                    categoryId = category.id
                                )
                                todoViewModel.addTodo(todo)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = content.isNotEmpty()
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}