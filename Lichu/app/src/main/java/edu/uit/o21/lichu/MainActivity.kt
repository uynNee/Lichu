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
import androidx.compose.foundation.layout.padding
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
        CategoryList()
        BottomNavBar(selectedTabIndex.intValue) { newIndex ->
            selectedTabIndex.intValue = newIndex
            // Handle navigation here
        }
    }
}

@Composable
fun CategoryList() {
    Column {
        Text("Notes", style = MaterialTheme.typography.titleLarge)
        Category("Category 1", CategoryColors.Category1) {
            Note("Note 1.1", "Due Date 1.1", CategoryColors.Category1)
            Note("Note 1.2", "Due Date 1.2", CategoryColors.Category1)
            Note("Note 1.3", "Due Date 1.3", CategoryColors.Category1)
        }
        Category("Category 2", CategoryColors.Category2) {
            Note("Note 2.1", "Due Date 2.1", CategoryColors.Category2)
            Note("Note 2.2", "Due Date 2.2", CategoryColors.Category2)
            Note("Note 2.3", "Due Date 2.3", CategoryColors.Category2)
        }
        Category("Category 3", CategoryColors.Category3) {
            Note("Note 3.1", "Due Date 3.1", CategoryColors.Category3)
            Note("Note 3.2", "Due Date 3.2", CategoryColors.Category3)
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
            icon = { Icon(painter = painterResource(id = android.R.drawable.ic_menu_gallery), contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = selectedTabIndex == 0,
            onClick = { onItemClick(0) }
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = android.R.drawable.ic_menu_month), contentDescription = "Calendar") },
            label = { Text("Calendar") },
            selected = selectedTabIndex == 1,
            onClick = { onItemClick(1) }
        )
    }
}

@Composable
fun Category(header: String, colors: CategoryColors, notes: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(colors.backgroundColor)
    ) {
        Text(header, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
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
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f).padding(start = 12.dp)
            )
            Text(
                dueDate,
                style = MaterialTheme.typography.bodySmall,
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