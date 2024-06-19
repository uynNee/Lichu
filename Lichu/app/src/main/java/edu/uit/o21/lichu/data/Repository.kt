package edu.uit.o21.lichu.data

import edu.uit.o21.lichu.data.dao.CategoryDao
import edu.uit.o21.lichu.data.dao.ToDoDao
import edu.uit.o21.lichu.data.entity.Category
import edu.uit.o21.lichu.data.entity.ToDo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

class Repository (
    private val categoryDao: CategoryDao,
    private val todoDao: ToDoDao? = null
){
    val categories = categoryDao.getAll()
    fun getTodos(categoryId: Int) = todoDao?.getAll(categoryId)

    suspend fun insertCategory(category: Category) = categoryDao.insert(category)
    suspend fun updateCategory(category: Category) = categoryDao.update(category)
    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    suspend fun insertTodo(todo: ToDo) = todoDao?.insert(todo)
    suspend fun updateTodo(todo: ToDo) = todoDao?.update(todo)
    suspend fun deleteTodo(todo: ToDo) = todoDao?.delete(todo)

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun search(query: String): Flow<List<Category>> {
        val categoriesByName = categoryDao.findByName("%$query%")
        val categoryIdsByContent = todoDao?.findByContent("%$query%")
        val categoriesByContent = categoryIdsByContent!!.flatMapLatest { ids ->
            if (ids.isEmpty()) {
                flowOf(emptyList())
            } else {
                categoryDao.findById(ids)
            }
        }
        return categoriesByName.combine(categoriesByContent) { list1, list2 ->
            (list1 + list2).distinctBy { it.categoryId }
        }
    }
}