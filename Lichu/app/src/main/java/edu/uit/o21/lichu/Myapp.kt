package edu.uit.o21.lichu

import android.app.Application
import edu.uit.o21.lichu.data.Repository
import edu.uit.o21.lichu.data.dao.CategoryDao
import edu.uit.o21.lichu.data.dao.ToDoDao
import edu.uit.o21.lichu.data.database.DbConnection

class Myapp: Application() {
    val database by lazy{DbConnection.getDatabase(this)}
    private val categoryDao: CategoryDao by lazy { database.categoryDao() }
    private val todoDao: ToDoDao? by lazy { database.toDoDao() }
    val repository by lazy { Repository(categoryDao, todoDao) }
}