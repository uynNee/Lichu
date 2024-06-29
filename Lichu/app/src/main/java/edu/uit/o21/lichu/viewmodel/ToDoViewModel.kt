package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.data.entity.ToDo

class ToDoViewModel: ViewModel(){
    val toDoViewModel = MainApplication.dbConnection.getTodoDao()

    fun todoList(categoryId:Int): LiveData<List<ToDo>> {
        return toDoViewModel.getAll(categoryId)
    }

    fun addTodo(toDo: ToDo){
        toDoViewModel.insert(
            ToDo(
                content=toDo.content,
                isDone = toDo.isDone,
                startTime = toDo.startTime,
                endTime = toDo.endTime,
                categoryId = toDo.categoryId)
        )
    }
}