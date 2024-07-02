package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.data.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel: ViewModel(){
    private val toDoViewModel = MainApplication.dbConnection.getTodoDao()

    fun todoList(categoryId:Int): LiveData<List<ToDo>> {
        return toDoViewModel.getAll(categoryId)
    }

    fun addTodo(toDo: ToDo){
        viewModelScope.launch (Dispatchers.IO){
            toDoViewModel.insert(ToDo(content=toDo.content, isDone = toDo.isDone, startTime = toDo.startTime, endTime = toDo.endTime, categoryId = toDo.categoryId))
        }
    }
}