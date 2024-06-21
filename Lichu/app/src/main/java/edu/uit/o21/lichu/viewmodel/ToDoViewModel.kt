package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.uit.o21.lichu.data.Repository
import edu.uit.o21.lichu.data.entity.ToDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ToDoViewModel (private val resository:Repository) : ViewModel(){
    fun getToDosForList(listId: Int): Flow<List<ToDo>>? = resository.getTodos(listId)
    fun getToDoByContent(content: String): Flow<List<Int>>? = resository.getToDoByContent(content)

    fun insert(todo: ToDo) = viewModelScope.launch {
        resository.insertTodo(todo)
    }
    fun delete(todo: ToDo) = viewModelScope.launch {
        resository.deleteTodo(todo)
    }
    fun update(todo: ToDo) = viewModelScope.launch {
        resository.updateTodo(todo)
    }
}
class ToDoViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}