package edu.uit.o21.lichu.ui.view.secondaryscreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.uit.o21.lichu.data.entity.ToDo
import edu.uit.o21.lichu.viewmodel.CategoryViewModel
import edu.uit.o21.lichu.viewmodel.ToDoViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryOnclickScreen(
    navController: NavController,
    categoryId: Int,
    categoryName: String
) {
    val toDoViewModel: ToDoViewModel = viewModel()
    val toDoList by toDoViewModel.todoList(categoryId).observeAsState(emptyList())
    val categoryViewModel: CategoryViewModel = viewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var showDialog by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                categoryName,
                TextRange(categoryName.length)
            )
        )
    }
    val categoryState = categoryViewModel.checkName(textFieldValue.text).observeAsState()
    val context = LocalContext.current
    val temp = TextFieldValue(
        text = categoryName,
        selection = TextRange(categoryName.length)
    )
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = {
                Text(
                    "Are you sure you want to delete this category?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    categoryViewModel.deleteCategory(categoryId)
                    showDialog = false
                    navController.navigate("ToDoScreen")
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                    if (textFieldValue.text == "") {
                        Toast.makeText(
                            context,
                            "Category's name cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                        textFieldValue = TextFieldValue(
                            categoryName,
                            TextRange(categoryName.length)
                        )
                    }
                    if (textFieldValue.text != categoryName) {
                        if (categoryState.value == false) {
                            categoryViewModel.updateCategory(categoryId, textFieldValue.text)
                            Toast
                                .makeText(
                                    context,
                                    "Successfully changed category's name",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            textFieldValue = temp
                            Toast
                                .makeText(
                                    context,
                                    "This category's name is duplicated",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                })
            },
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = {
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                })
                            }
                    ) {
                        TextField(
                            value = textFieldValue,
                            onValueChange = { newValue ->
                                textFieldValue = newValue
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (textFieldValue.text == "") {
                                        Toast.makeText(
                                            context,
                                            "Category's name cannot be empty",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        textFieldValue = TextFieldValue(
                                            categoryName,
                                            TextRange(categoryName.length)
                                        )
                                    }
                                    if (categoryState.value == false) {
                                        categoryViewModel.updateCategory(
                                            categoryId,
                                            textFieldValue.text
                                        )
                                        Toast.makeText(
                                            context,
                                            "Successfully changed category's name",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        textFieldValue = temp
                                        Toast.makeText(
                                            context,
                                            "This category's name is duplicated",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = (-16).dp)
                                .focusRequester(focusRequester)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate("ToDoScreen") },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("AddTodoWithCategoryIdScreen/$categoryId")
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                    }
                    IconButton(
                        onClick = {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                            textFieldValue = textFieldValue.copy(
                                selection = TextRange(textFieldValue.text.length)
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Title")
                    }
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val sortedToDoList = toDoList.sortedBy { it.isDone }

            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(sortedToDoList) { todo ->
                    ToDoItem(
                        todo = todo,
                        navController = navController,
                        toDoViewModel = toDoViewModel
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ToDoItem(todo: ToDo, navController: NavController, toDoViewModel: ToDoViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = {
                Text(
                    "Are you sure you want to delete this todo?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    toDoViewModel.deleteToDo(todo.id)
                    showDialog = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { isChecked ->
                val updatedTodo = todo.copy(isDone = isChecked)
                toDoViewModel.updateCheck(updatedTodo)
            },
            modifier = Modifier.padding(end = 16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = todo.content,
                style = if (todo.isDone) {
                    MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                } else {
                    MaterialTheme.typography.bodyLarge
                }
            )
            Text(
                text = if (todo.endTime != null) {
                    buildAnnotatedString {
                        append("Deadline: ")
                        todo.endTime.format(dateFormatter).let { append(it) }
                    }
                } else {
                    buildAnnotatedString {
                        append("No deadline set")
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = {
                val todoId = Uri.encode(todo.id.toString())
                navController.navigate("EditTodoScreen/$todoId")
            }
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        }
        IconButton(
            onClick = {
                showDialog = true
            }
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}