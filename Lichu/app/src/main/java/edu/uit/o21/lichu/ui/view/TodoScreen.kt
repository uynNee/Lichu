package edu.uit.o21.lichu.ui.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.uit.o21.lichu.data.entity.Category
import edu.uit.o21.lichu.ui.MyApp
import edu.uit.o21.lichu.ui.theme.LichuTheme
import edu.uit.o21.lichu.viewmodel.CategoryViewModel
import edu.uit.o21.lichu.viewmodel.ToDoViewModel
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodolistScreen(navController:NavController) {
    val categoryViewModel:CategoryViewModel = viewModel()
    val categoryList by categoryViewModel.categoryList.observeAsState(emptyList())
    Scaffold(
        topBar = {SearchBar()},
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            LazyColumn {
                itemsIndexed(categoryList) { _: Int, item: Category ->
                    CategoryItems(item = item,navController)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryItems(item: Category,navController:NavController) {
    val toDoViewModel:ToDoViewModel = viewModel()
    val toDoList by toDoViewModel.todoList(item.id).observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                navController.navigate("CategoryOnclickScreen")
            }
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        var count = 0
        toDoList.filter { !it.isDone }.take(3).forEach { todo ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = todo.isDone,
                    onCheckedChange = { /* Handle checkbox change */ },
                )
                Text(
                    text = todo.content,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                if(todo.endTime!=null){
                    val daysBetween = ChronoUnit.DAYS.between(todo.startTime, todo.endTime)
                    val monthsBetween = daysBetween / 30
                    val yearsBetween = monthsBetween / 12
                    if (yearsBetween != 0L) {
                        val yearsText = buildString {
                            append("$yearsBetween ")
                            append(if (yearsBetween < 1) "years" else "year")
                            if ((monthsBetween % 12).toInt() !=0) {
                                append("+")
                            }
                        }
                        Text(text = yearsText)
                    }
                    else if (monthsBetween != 0L) {
                        val monthText = buildString {
                            append("$monthsBetween ")
                            append(if (monthsBetween > 1) "months" else "month")
                            if ((daysBetween % 30).toInt() !=0) {
                                append("+")
                            }
                        }
                        Text(text = monthText)
                    }
                    else if (daysBetween != 0L) {
                        val daysText =  if (daysBetween > 1) "$daysBetween days"
                                        else "$daysBetween day"
                        Text(text = daysText)
                    }
                    else {
                        Text(text = "Due")
                    }
                }
            }
            count++
        }
    }
}

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    Color.Gray.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(36.dp)
                )
                .padding(start = 6.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search something...",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                )

                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
        if (isFocused) {
            TextButton(
                onClick = {
                    searchQuery = ""
                    isFocused = false
                    focusManager.clearFocus() // Clear focus
                    keyboardController?.hide() // Hide keyboard
                },
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(text = "Cancel", textAlign = TextAlign.Center)
            }
        }
    }
}