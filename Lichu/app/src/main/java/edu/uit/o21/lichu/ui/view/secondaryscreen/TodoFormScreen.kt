package edu.uit.o21.lichu.ui.view.secondaryscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import edu.uit.o21.lichu.data.entity.Category
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
fun TodoFormScreen(
    navController: NavController,
    todoViewModel: ToDoViewModel = viewModel(),
    categoryViewModel: CategoryViewModel = viewModel(),
    isEdit: Boolean = false,
    isAddToDo: Boolean = false,
    categoryId: Int? = null,
    initialTodo: ToDo? = null
) {
    var content by remember { mutableStateOf(initialTodo?.content ?: "") }
    var endDate by remember { mutableStateOf(initialTodo?.endTime) }
    val currentDate = LocalDate.now()
    var dateCheck by remember { mutableStateOf(true) }

    var expanded by remember { mutableStateOf(false) }
    val categoryListState = categoryViewModel.categoryList.observeAsState(emptyList())
    val categoryList = categoryListState.value
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    LaunchedEffect(categoryList) {
        if (isEdit)
            selectedCategory = categoryList.find { it.id == initialTodo?.categoryId }
        if (isAddToDo)
            selectedCategory = categoryList.find { it.id == categoryId }
    }

    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var isContentEmpty by remember { mutableStateOf(content.isEmpty()) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Todo" else "Add Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
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
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
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
                selectedDate = endDate,
                onDateSelected = {
                    endDate = it
                    dateCheck = it?.let { date -> date >= currentDate } ?: true
                },
                dateFormatter = dateFormatter
            )
            if (!dateCheck) {
                Text(
                    text = "End date cannot be less than today",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .padding(top = 8.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (!isEdit && !isAddToDo) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedCategory?.name ?: "Please select a category",
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
                                } else if (isAddToDo) {
                                    val newTodo = categoryId?.let {
                                        ToDo(
                                            content = content,
                                            startTime = currentDate,
                                            endTime = endDate,
                                            isDone = false,
                                            categoryId = it
                                        )
                                    }
                                    if (newTodo != null) {
                                        todoViewModel.addTodo(newTodo)
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
                    enabled = content.isNotEmpty() && dateCheck && ((selectedCategory != null) || isEdit || isAddToDo)
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerField(
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
                    LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth)
                )
            },
            year,
            month,
            day
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                datePickerDialog.show()
            })
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(4.dp)
            )
            .height(60.dp),

        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (selectedDate != null) {
            Text(
                text = selectedDate.format(dateFormatter),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            )

            IconButton(
                onClick = {
                    onDateSelected(null)
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
            }
        } else {
            Text(
                text = "Select a date",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp)
            )
        }
    }
}