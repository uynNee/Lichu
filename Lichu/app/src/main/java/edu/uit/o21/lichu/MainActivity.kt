package edu.uit.o21.lichu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uit.o21.lichu.ui.theme.LichuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LichuTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val selectedTabIndex = remember { mutableIntStateOf(0) }
    val categories = remember { mutableStateListOf(*categoriesList.toTypedArray()) }
    val isNoteDialogOpen = remember { mutableStateOf(false) }
    val selectedNote = remember { mutableStateOf<NoteData?>(null) }

    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomNavBar(selectedTabIndex.intValue) { newIndex ->
                selectedTabIndex.intValue = newIndex
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            CategoryList(categories, onNoteClick = { note ->
                isNoteDialogOpen.value = true
                selectedNote.value = note
            })
        }
        if (isNoteDialogOpen.value) {
            NoteDialog(
                note = selectedNote.value!!,
                onConfirm = { isNoteDialogOpen.value = false },
                onClose = { isNoteDialogOpen.value = false }
            )
        }
    }
}

data class CategoryData(val name: String, val notes: MutableList<NoteData>)
data class NoteData(var header: String, var dueDate: String)

val categoriesList = listOf(
    CategoryData("School - Week 10", mutableListOf(
        NoteData("Chemistry Test", "Tomorrow"),
        NoteData("Science Test", "Tomorrow"),
        NoteData("English Test", "Tomorrow")
    )),
    CategoryData("Groceries", mutableListOf(
        NoteData("Shampoo", ""),
        NoteData("Cat food", "")
    )),
    CategoryData("Notes", mutableListOf(
        NoteData("Emergency", "11/10"),
        NoteData("*Locked*", "")
    )),
    CategoryData("Category", mutableListOf(
        NoteData("Note", "Date"),
        NoteData("Note", "Date"),
        NoteData("Note", "Date")
    )),
    CategoryData("Category", mutableListOf(
        NoteData("Note", "Date"),
        NoteData("Note", "Date"),
        NoteData("Note", "Date")
    )),
)

@Composable
fun CategoryList(categories: MutableList<CategoryData>, onNoteClick: (NoteData) -> Unit) {
    LazyColumn {
        items(categories) { category ->
            Category(category.name) {
                category.notes.forEach { note ->
                    Note(note.header, note.dueDate, onClick = { onNoteClick(note) })
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Text("Your Notes", fontSize = 32.sp, modifier = Modifier.padding(8.dp, 2.dp))
}

@Composable
fun BottomNavBar(
    selectedTabIndex: Int,
    onItemClick: (Int) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Notes",
                    modifier = Modifier.offset(0.dp, (-3).dp)
                )
            },
            label = {
                Text(
                    "Notes",
                    fontSize = 18.sp,
                    modifier = Modifier.offset(y = 8.dp)
                )
            },
            selected = selectedTabIndex == 0,
            onClick = { onItemClick(0) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_month),
                    contentDescription = "Calendar",
                    modifier = Modifier.offset(0.dp, (-3).dp)
                )
            },
            label = {
                Text(
                    "Calendar",
                    fontSize = 18.sp,
                    modifier = Modifier.offset(y = 8.dp)
                )
            },
            selected = selectedTabIndex == 1,
            onClick = { onItemClick(1) }
        )
    }
}

@Composable
fun Category(header: String, notes: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(12.dp, 8.dp, 12.dp, 6.dp)
            .fillMaxWidth()
    ) {
        Text(header, fontSize = 24.sp, modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 0.dp))
        notes()
    }
}

@Composable
fun Note(header: String, dueDate: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                header,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            )
            Text(
                dueDate,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun NoteDialog(note: NoteData, onConfirm: () -> Unit, onClose: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Close",
                    modifier = Modifier.clickable(onClick = onClose)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            var header by remember { mutableStateOf(note.header) }
            TextField(
                value = header,
                onValueChange = { header = it; note.header = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            var dueDate by remember { mutableStateOf(note.dueDate) }
            TextField(
                value = dueDate,
                onValueChange = { dueDate = it; note.dueDate = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_save),
                contentDescription = "Confirm",
                modifier = Modifier.clickable(onClick = onConfirm)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    LichuTheme {
        MainScreen()
    }
}