package edu.uit.o21.lichu.ui.view.mainscreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import edu.uit.o21.lichu.data.entity.Category
import edu.uit.o21.lichu.viewmodel.CategoryViewModel
import edu.uit.o21.lichu.viewmodel.ToDoViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodolistScreen(navController: NavController) {
    val categoryViewModel: CategoryViewModel = viewModel()
    val toDoViewModel: ToDoViewModel = viewModel()
    val categoryList by categoryViewModel.categoryList.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }
    var searchQueryState by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }
    var isCategory by remember { mutableStateOf(true) }
    LaunchedEffect(searchQueryState) {
        delay(300)
        searchQuery = searchQueryState
    }
    Scaffold(
        topBar = {
            CustomSearchBar(
                searchQuery = searchQueryState,
                isFocused = isFocused,
                isCategory = isCategory,
                onSearchQueryChange = { query ->
                    searchQueryState = query
                },
                onFocusChange = { focusState ->
                    isFocused = focusState
                },
                onCategoryClick = { categoryState ->
                    isCategory = categoryState
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            isFocused = false
                        }
                    )
                }
                .padding(paddingValues)
        ) {
            val filteredCategories = categoryList.filter { category ->
                if (isCategory)
                    category.name.contains(searchQuery, ignoreCase = true) && !isFocused
                else {
                    val todos =
                        toDoViewModel.todoList(category.id).observeAsState(emptyList()).value
                    todos.any { todo ->
                        todo.content.contains(
                            searchQuery,
                            ignoreCase = true
                        ) && !isFocused
                    }
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(filteredCategories) { _, category ->
                    CategoryItems(item = category, navController, searchQuery, isCategory)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoryItems(
    item: Category,
    navController: NavController,
    searchQuery: String,
    isCategory: Boolean
) {
    val toDoViewModel: ToDoViewModel = viewModel()
    val toDoListState by toDoViewModel.todoList(item.id).observeAsState(emptyList())
    val toDoList = remember { mutableStateListOf(*toDoListState.toTypedArray()) }
    var isExpanded by remember { mutableStateOf(false) }
    var cap by remember { mutableIntStateOf(0) }
    LaunchedEffect(isExpanded) {
        cap = if (isExpanded) 99 else 3
    }
    LaunchedEffect(toDoListState) {
        toDoList.clear()
        toDoList.addAll(toDoListState)
    }
    val lineColor = MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val categoryId = Uri.encode(item.id.toString())
                val categoryName = Uri.encode(item.name)
                val route = "CategoryOnclickScreen/$categoryId/$categoryName"
                navController.navigate(route)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val todoListState = toDoViewModel.todoList(item.id)
                .observeAsState(emptyList()).value.filter { !it.isDone }
            if (todoListState.size > 3) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Arrow Up" else "Arrow Down",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .size(42.dp)
                        .clip(CircleShape)
                        .padding(horizontal = 4.dp)
                )
            }
        }
        var count = 0
        val toDosList = toDoList.filter {
            if (!isCategory) it.content.contains(searchQuery, ignoreCase = true)
            else true
        }
        Spacer(modifier = Modifier.height(8.dp))
        for (todo in toDosList) {
            var isChecked by remember(todo.id) { mutableStateOf(todo.isDone) }
            var visible by rememberSaveable { mutableStateOf(true) }
            val lineAnimationProgress by animateFloatAsState(
                targetValue = if (isChecked) 1f else 0f,
                animationSpec = tween(durationMillis = 500), label = "slideAnimation"
            )
            LaunchedEffect(isChecked) {
                if (isChecked) {
                    delay(700)
                    visible = false
                }
                if (!isChecked) {
                    visible = true
                }
            }
            if (!visible) continue
            if (count == cap) continue
            count++
            Box(modifier = Modifier.fillMaxWidth()) {
                var rowHeight by remember { mutableFloatStateOf(0f) }
                Layout(
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    isChecked = checked
                                    val updatedTodo = todo.copy(isDone = checked)
                                    toDoViewModel.updateCheck(updatedTodo)
                                }
                            )
                            Text(
                                text = todo.content,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            if (todo.endTime != null) {
                                val curDay = LocalDate.now()
                                val daysBetween = ChronoUnit.DAYS.between(curDay, todo.endTime)
                                val monthsBetween = daysBetween / 30
                                val yearsBetween = monthsBetween / 12
                                when {
                                    yearsBetween != 0L -> {
                                        val yearsText = buildString {
                                            append("$yearsBetween ")
                                            append(if (yearsBetween > 1) "years" else "year")
                                            if ((monthsBetween % 12).toInt() != 0) append("+")
                                        }
                                        Text(
                                            text = yearsText,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    monthsBetween != 0L -> {
                                        val monthText = buildString {
                                            append("$monthsBetween ")
                                            append(if (monthsBetween > 1) "months" else "month")
                                            if ((daysBetween % 30).toInt() != 0) append("+")
                                        }
                                        Text(
                                            text = monthText,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    daysBetween > 0L -> {
                                        val daysText =
                                            if (daysBetween > 1) "$daysBetween days" else "$daysBetween day"
                                        Text(
                                            text = daysText,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    daysBetween == 0L -> Text(
                                        text = "Due today",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    else -> Text(
                                        text = "Due",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { measurements, constraints ->
                    val placements = measurements.map { it.measure(constraints) }
                    rowHeight = placements.maxOfOrNull { it.height }?.toFloat() ?: 0f
                    layout(constraints.maxWidth, rowHeight.toInt()) {
                        placements.forEach { it.placeRelative(0, 0) }
                    }
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    val yPosition = rowHeight / 2
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, yPosition),
                        end = Offset(size.width * lineAnimationProgress, yPosition),
                        strokeWidth = 4f
                    )
                }
            }
        }
        if (count == 0) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text =
                    if (searchQuery.isNotEmpty()) "No corresponding Todo"
                    else "There is no Todo",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun CustomSearchBar(
    searchQuery: String,
    isFocused: Boolean,
    isCategory: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    onCategoryClick: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    Column {
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
                    .padding(start = 6.dp),
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
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                onFocusChange(focusState.isFocused)
                                if (!focusState.isFocused) {
                                    keyboardController?.hide()
                                }
                            },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                            color = onSurfaceColor
                        ),
                        cursorBrush = SolidColor(onSurfaceColor),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search something...",
                                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
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
                        onSearchQueryChange("")
                        onFocusChange(false)
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "Cancel",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        if (isFocused) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-8).dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .drawBehind {
                            if (isCategory) {
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                        .weight(1f)
                        .background(Color.Transparent)
                        .clickable { onCategoryClick(true) }
                        .padding(vertical = 12.dp)
                        .height(26.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Category",
                        color = if (isCategory) primaryColor else onSurfaceColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Box(
                    modifier = Modifier
                        .drawBehind {
                            if (!isCategory) {
                                drawLine(
                                    color = primaryColor,
                                    start = Offset(0f, size.height),
                                    end = Offset(size.width, size.height),
                                    strokeWidth = 2.dp.toPx()
                                )
                            }
                        }
                        .weight(1f)
                        .background(Color.Transparent)
                        .clickable { onCategoryClick(false) }
                        .padding(vertical = 12.dp)
                        .height(26.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Todo",
                        color = if (!isCategory) primaryColor else onSurfaceColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}