//package edu.uit.o21.lichu.ui.view
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.unit.dp
//import edu.uit.o21.lichu.data.entity.Category
//import edu.uit.o21.lichu.data.entity.getFakeCategory
//import edu.uit.o21.lichu.data.entity.getFakeToDo
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TodolistScreen() {
//    val categoryList = getFakeCategory()
//
//    var isSearchBarVisible by remember { mutableStateOf(true) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("To-Do List") },
//                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primary
//                )
//            )
//        },
//
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .padding(paddingValues)
//                .padding(16.dp)
//        ) {
//            // Điều kiện hiển thị thanh tìm kiếm dựa trên trạng thái isSearchBarVisible
//            if (isSearchBarVisible) {
//                SearchBar()
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//            LazyColumn {
//                itemsIndexed(categoryList) { _: Int, item: Category ->
//                    CategoryItems(item = item)
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun SearchBar() {
//    val textState = remember { mutableStateOf("") }
//
//    OutlinedTextField(
//        value = textState.value,
//        onValueChange = { textState.value = it },
//        label = { Text("Enter text") },
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(bottom = 14.dp)
//    )
//}
//
//@Composable
//fun CategoryItems(item: Category) {
//    val toDoList = getFakeToDo()
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clip(RoundedCornerShape(16.dp))
//            .background(MaterialTheme.colorScheme.primaryContainer)
//            .padding(16.dp)
//    ) {
//        Text(
//            text = item.name,
//            style = MaterialTheme.typography.headlineSmall,
//            color = MaterialTheme.colorScheme.onPrimaryContainer
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        var count = 0 // Counter for displayed items
//        toDoList.filter { !it.isDone }.take(3).forEach { todo ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(vertical = 4.dp)
//            ) {
//                Checkbox(
//                    checked = todo.isDone,
//                    onCheckedChange = { /* Handle checkbox change */ },
//                    modifier = Modifier.padding(start = 8.dp)
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = todo.content,
//                    modifier = Modifier.weight(1f)
//                )
//            }
//            count++
//        }
//    }
//}
