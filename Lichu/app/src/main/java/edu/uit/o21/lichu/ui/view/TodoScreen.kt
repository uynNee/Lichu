package edu.uit.o21.lichu.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import edu.uit.o21.lichu.data.entity.Category
import edu.uit.o21.lichu.data.entity.getFakeCategory
import edu.uit.o21.lichu.data.entity.getFakeToDo

@Composable
fun TodolistScreen() {
    val categoryList= getFakeCategory()

    Column (
        modifier= Modifier
            .fillMaxHeight()
            .padding(25.dp)
    ){
        LazyColumn(
            content = {
                itemsIndexed(categoryList){ _: Int, item: Category ->
                    CategoryItems(item=item)
                }
            }
        )
    }
}
@Composable
fun CategoryItems(item:Category){
    val toDoList= getFakeToDo()
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
    ){
            Column {
            Text(text = item.name)
            Spacer(modifier = Modifier.width(16.dp))
                toDoList.forEach { todo ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = todo.isDone,
                            onCheckedChange = { isChecked ->
                                todo.isDone = isChecked
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = todo.content,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
    }
}