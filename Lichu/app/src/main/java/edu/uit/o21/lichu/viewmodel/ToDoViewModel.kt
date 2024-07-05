package edu.uit.o21.lichu.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.data.dao.ToDoDao
import edu.uit.o21.lichu.data.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel: ViewModel(){
    private val toDoViewModel=MainApplication.dbConnection.getTodoDao()

    fun calendarTodoList(): LiveData<List<ToDo>> {
        return toDoViewModel.calendarGetAll()
    }

    fun todoList(categoryId:Int): LiveData<List<ToDo>> {
        return toDoViewModel.getAll(categoryId)
    }

    fun todoById(todoId: Int): LiveData<ToDo> {
        return toDoViewModel.getToDoById(todoId)
    }

    fun addTodo(toDo: ToDo){
        viewModelScope.launch (Dispatchers.IO){
            toDoViewModel.insert(ToDo(content=toDo.content, isDone = toDo.isDone, startTime = toDo.startTime, endTime = toDo.endTime, categoryId = toDo.categoryId))
        }
    }
    fun updateToDo(todo: ToDo){
        viewModelScope.launch (Dispatchers.IO){
            toDoViewModel.update(todo.id,todo.content,todo.endTime)
        }
    }
    fun deleteToDo(id:Int){
        viewModelScope.launch (Dispatchers.IO){
            toDoViewModel.delete(id)
        }
    }
}