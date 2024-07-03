package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.data.dao.ToDoDao
import edu.uit.o21.lichu.data.entity.ToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel : ViewModel() {
    private val toDoDao: ToDoDao = MainApplication.dbConnection.getTodoDao()

    fun calendarTodoList(): LiveData<List<ToDo>> {
        return toDoDao.calendarGetAll()
    }

    fun todoList(categoryId: Int): LiveData<List<ToDo>> {
        return toDoDao.getAll(categoryId)
    }

    fun addTodo(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            toDoDao.insert(
                ToDo(
                    content = todo.content,
                    isDone = todo.isDone,
                    startTime = todo.startTime,
                    endTime = todo.endTime,
                    categoryId = todo.categoryId
                )
            )
        }
    }
}