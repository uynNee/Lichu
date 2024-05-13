package edu.uit.o21.lichu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uit.o21.lichu.ui.theme.LichuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LichuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Text(
        "To do list",
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 12.dp, top = 32.dp),
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun BottomNavBar(selectedTabIndex: Int, onItemClick: (Int) -> Unit) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(MaterialTheme.shapes.large),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "To do list",
                )
            },
            modifier = Modifier.padding(top = 10.dp),
            selected = selectedTabIndex == 0,
            onClick = { onItemClick(0) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_month),
                    contentDescription = "Calendar",
                )
            },
            modifier = Modifier.padding(top = 10.dp),
            selected = selectedTabIndex == 1,
            onClick = { onItemClick(1) }
        )
    }
}

val categoriesList = listOf(
    CategoryData(
        "School - Week 10", mutableListOf(
            ToDoData("Chemistry Test", "Tomorrow"),
            ToDoData("Science Test", "Tomorrow"),
            ToDoData("English Test", "Tomorrow")
        )
    ),
    CategoryData(
        "Groceries", mutableListOf(
            ToDoData("Shampoo", ""),
            ToDoData("Cat food", "")
        )
    ),
    CategoryData(
        "Todo", mutableListOf(
            ToDoData("Emergency", "11/10"),
            ToDoData("*Locked*", "")
        )
    ),
    CategoryData(
        "Category", mutableListOf(
            ToDoData("Note", "Date")
        )
    ),
)

@Composable
fun MainScreen() {
    val selectedTabIndex = remember { mutableIntStateOf(0) }
    val allCategories = remember { mutableStateListOf(*categoriesList.toTypedArray()) }
    val isToDoDialogOpen = remember { mutableStateOf(false) }
    val selectedToDo = remember { mutableStateOf<ToDoData?>(null) }
    val selectedCategory = remember { mutableStateOf<CategoryData?>(null) }
    var ifAddToDo = false

    Scaffold(
        topBar = { TopBar() },
        bottomBar = {
            BottomNavBar(selectedTabIndex.intValue) { newIndex ->
                selectedTabIndex.intValue = newIndex
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            CategoryList(
                categories = allCategories,
                onToDoClick = { category, todo ->
                    isToDoDialogOpen.value = true
                    selectedToDo.value = todo
                    selectedCategory.value = category
                    ifAddToDo = false
                },
                onAddToDo = { category ->
                    isToDoDialogOpen.value = true
                    selectedCategory.value = category
                    selectedToDo.value = ToDoData("", "")
                    ifAddToDo = true
                }
            )
        }

        if (isToDoDialogOpen.value) {
            ToDoDialog(
                ifAddToDo,
                category = selectedCategory.value ?: CategoryData("", mutableListOf()),
                toDo = selectedToDo.value ?: ToDoData("", ""),
                onConfirm = { isToDoDialogOpen.value = false },
                onClose = { isToDoDialogOpen.value = false }
            )
        }
    }
}

data class CategoryData(val name: String, val toDos: MutableList<ToDoData>)
data class ToDoData(var header: String, var dueDate: String)

@Composable
fun CategoryList(
    categories: MutableList<CategoryData>,
    onToDoClick: (CategoryData, ToDoData) -> Unit,
    onAddToDo: (CategoryData) -> Unit
) {
    LazyColumn {
        items(categories) { category ->
            Category(category, {
                category.toDos.forEach { todo ->
                    ToDoNote(todo.header, todo.dueDate, onClick = { onToDoClick(category, todo) })
                }
            }) {
                onAddToDo(category)
            }
        }
    }
}

@Composable
fun Category(
    category: CategoryData,
    toDos: @Composable () -> Unit,
    onAddToDo: (CategoryData) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp, 8.dp, 16.dp, 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.outlineVariant)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                category.name,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 2.dp),
                color = MaterialTheme.colorScheme.inverseSurface
            )
            Button(
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.outlineVariant),
                onClick = { onAddToDo(category) }
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Add",
                    tint = MaterialTheme.colorScheme.inverseSurface
                )
            }
        }
        toDos()
    }
}

@Composable
fun ToDoNote(header: String, dueDate: String, onClick: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSecondary)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.secondary,
                    uncheckedColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                header,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp, top = 2.dp)
            )
            Text(
                dueDate,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun ToDoDialog(
    isAddToDo: Boolean,
    category: CategoryData,
    toDo: ToDoData,
    onConfirm: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 100.dp)
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Edit Note", fontSize = 28.sp)
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Close",
                    modifier = Modifier.clickable(onClick = onClose)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            var header by remember { mutableStateOf(toDo.header) }
            TextField(
                value = header,
                onValueChange = { header = it; toDo.header = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(20.dp, 0.dp),
                singleLine = false,
            )
            Spacer(modifier = Modifier.height(16.dp))
            var dueDate by remember { mutableStateOf(toDo.dueDate) }
            TextField(
                value = dueDate,
                onValueChange = { dueDate = it; toDo.dueDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(20.dp, 0.dp),
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_save),
                    contentDescription = "Confirm",
                    modifier = Modifier.clickable {
//                        if(isAddToDo && header.isEmpty() && dueDate.isEmpty()){
//                              return error message saying note cannot be blank
//                        }
                        if (isAddToDo && (header.isNotEmpty() || dueDate.isNotEmpty())) {
                            category.toDos.add(toDo)
                        }
                        onConfirm()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    LichuTheme(darkTheme = true) {
        MainScreen()
    }
}