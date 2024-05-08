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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uit.o21.lichu.ui.theme.LichuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LichuTheme {
                // A surface container using the 'background' color from the theme
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

data class CategoryColors(val background: Color, val note: Color)

val category1Colors = CategoryColors(Color(0xFFD8A6FA), Color(0xFFDCD7FF))
val category2Colors = CategoryColors(Color(0xFFBBA6FA), Color(0xFFE0D7FF))
val category3Colors = CategoryColors(Color(0xFFA6B3FA), Color(0xFFD7DEFF))


@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Category("Category 1", category1Colors) {
            Note("Note 1.1", "Due Date 1.1", category1Colors)
            Note("Note 1.2", "Due Date 1.2", category1Colors)
            Note("Note 1.3", "Due Date 1.3", category1Colors)
        }
        Category("Category 2", category2Colors) {
            Note("Note 2.1", "Due Date 2.1", color = category2Colors)
            Note("Note 2.2", "Due Date 2.2", color = category2Colors)
            Note("Note 2.3", "Due Date 2.3", color = category2Colors)
        }
        Category("Category 3", category3Colors) {
            Note("Note 3.1", "Due Date 3.1", color = category3Colors)
            Note("Note 3.2", "Due Date 3.2", color = category3Colors)
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { /* Do something for Notes view */ }) {
                Text("Notes")
            }
            Button(onClick = { /* Do something for Calendar view */ }) {
                Text("Calendar")
            }
        }
    }
}

@Composable
fun Category(header: String, colors: CategoryColors, notes: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(colors.background)
    ) {
        Text(header, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        notes()
    }
}

@Composable
fun Note(header: String, dueDate: String, color: CategoryColors) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(color.note)
    ) {
        Row {
            Text(header, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.width(8.dp))
            Text(dueDate, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(4.dp))
        // Add more details or actions for the note
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LichuTheme {
        MainScreen()
    }
}