package edu.uit.o21.lichu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import edu.uit.o21.lichu.data.dao.CategoryDao
import edu.uit.o21.lichu.data.dao.ToDoDao
import edu.uit.o21.lichu.data.database.DbConnection
import edu.uit.o21.lichu.ui.theme.LichuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LichuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MyApp()
                }
            }
        }
    }
}
//@Preview
//@Composable
//fun MainScreenPreview() {
//
//    LichuTheme(darkTheme = true) {
//        MyApp(repository)
//    }
//}
