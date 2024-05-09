package edu.uit.o21.lichu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

sealed class CategoryColors(val backgroundColor: Color, val noteColor: Color) {
    data object Category1 : CategoryColors(Color(0xFFD8A6FA), Color(0xFFF0D3FD))
    data object Category2 : CategoryColors(Color(0xFFBBA6FA), Color(0xFFE5D7FF))
    data object Category3 : CategoryColors(Color(0xFFA6B3FA), Color(0xFFD7D6FF))
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val selectedTabIndex = remember { mutableIntStateOf(0) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column{
            Text("Your Notes", fontSize = 32.sp, modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp))
            CategoryList()
        }
        BottomNavBar(selectedTabIndex.intValue) { newIndex ->
            selectedTabIndex.intValue = newIndex
            // Handle navigation here
        }
    }
}

data class CategoryData(val name: String, val colors: CategoryColors, val notes: List<NoteData>)
data class NoteData(val header: String, val dueDate: String)

val categories = listOf(
    CategoryData("School - Week 10", CategoryColors.Category1, listOf(
        NoteData("Chemistry Test", "Tomorrow"),
        NoteData("Science Fair", "In 5 days"),
        NoteData("Homework", "Next month")
    )),
    CategoryData("Groceries", CategoryColors.Category2, listOf(
        NoteData("Shampoo", ""),
        NoteData("Batteries", ""),
        NoteData("Cat food", "")
    )),
    CategoryData("Notes", CategoryColors.Category3, listOf(
        NoteData("11/10", ""),
        NoteData("Emergency Meeting", "15/12")
    ))
)

@Composable
fun CategoryList() {
    LazyColumn {
        items(categories) { category ->
            Category(category.name, category.colors) {
                category.notes.forEach { note ->
                    Note(note.header, note.dueDate, category.colors)
                }
            }
        }
    }
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
                    modifier = Modifier.offset(y = 4.dp)
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
                    modifier = Modifier.offset(y = 4.dp)
                )
            },
            selected = selectedTabIndex == 1,
            onClick = { onItemClick(1) }
        )
    }
}

@Composable
fun Category(header: String, colors: CategoryColors, notes: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp, 12.dp, 16.dp, 6.dp)
            .fillMaxWidth()
            .background(colors.backgroundColor)
    ) {
        Text(header, fontSize = 24.sp, modifier = Modifier.padding(8.dp, 4.dp, 0.dp, 0.dp))
        notes()
    }
}

@Composable
fun Note(header: String, dueDate: String, colors: CategoryColors) {
    Column(
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .background(colors.noteColor)
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    LichuTheme {
        MainScreen()
    }
}