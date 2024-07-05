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
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
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
fun TodoFeatureScreen(
    navController: NavController,
    todoViewModel: ToDoViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    isEdit: Boolean = false,
    initialTodo: ToDo? = null
) {
    var content by remember { mutableStateOf(initialTodo?.content ?: "") }
    var endDate by remember { mutableStateOf(initialTodo?.endTime) }
    val currentDate = LocalDate.now()
    var dateCheck by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    val categoryListState = categoryViewModel.categoryList.observeAsState(emptyList())
    val categoryList = categoryListState.value
    var selectedCategory by remember(categoryList) {
        mutableStateOf(
            if (isEdit) categoryList.find { it.id == initialTodo?.categoryId }
            else if (categoryList.isNotEmpty()) categoryList[0]
            else null
        )
    }
    LaunchedEffect(categoryListState) {
        if (!isEdit && categoryList.isNotEmpty()) {
            selectedCategory = categoryList[0]
        }
    }
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var isContentEmpty by remember { mutableStateOf(content.isEmpty()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Todo" else "Add Todo") },
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
                text = if (isEdit) "Edit the todo" else "Create a new todo",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = content,
                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedLabelColor = Color.Transparent,
                    unfocusedLabelColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                onValueChange = {
                    content = it
                    isContentEmpty = it.isEmpty()
                },
                label = { Text("Content") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerField(
                label = "End Date",
                selectedDate = endDate,
                onDateSelected = {
                    endDate = it
                    dateCheck = endDate!! >= currentDate
                },
                dateFormatter = dateFormatter
            )
            if (!dateCheck) {
                Text(
                    text = "End date cannot be less than today",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 16.dp).padding(top = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
            if(!isEdit){
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    selectedCategory?.let {
                        TextField(
                            value = it.name,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Select Category") },
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
                        if (content.isNotEmpty() && dateCheck) {
                            selectedCategory?.let { category ->
                                if (isEdit) {
                                    initialTodo?.let { todo ->
                                        val updatedTodo = todo.copy(
                                            content = content,
                                            endTime = endDate,
                                            categoryId = category.id
                                        )
                                        todoViewModel.updateToDo(updatedTodo)
                                    }
                                } else {
                                    val newTodo = ToDo(
                                        content = content,
                                        startTime = currentDate,
                                        endTime = endDate,
                                        isDone = false,
                                        categoryId = category.id
                                    )
                                    todoViewModel.addTodo(newTodo)
                                }
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = content.isNotEmpty() && dateCheck
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
fun DatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit,
    dateFormatter: DateTimeFormatter
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                onDateSelected(
                    LocalDate.of(selectedYear,
                    selectedMonth + 1,
                    selectedDayOfMonth))
            },
            year,
            month,
            day
        )
    }
    OutlinedTextField(
        value = selectedDate?.format(dateFormatter) ?: "",
        onValueChange = { },
        readOnly = true,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                datePickerDialog.show()
            }),
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )
}