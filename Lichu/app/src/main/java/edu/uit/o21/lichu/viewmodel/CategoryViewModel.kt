package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.data.dao.CategoryDao
import edu.uit.o21.lichu.data.entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {
    private val categoryDao: CategoryDao = MainApplication.dbConnection.getCategoryDao()

    val categoryList: LiveData<List<Category>> = categoryDao.getAll()

    fun checkName(name: String): LiveData<Boolean> {
        return categoryDao.checkName(name)
    }

    fun addCategoryGetId(name: String): LiveData<Long> {
        val result = MutableLiveData<Long>()
        viewModelScope.launch(Dispatchers.IO) {
            val id = categoryDao.insertGetId(Category(name = name))
            result.postValue(id)
        }
        return result
    }

    fun updateCategory(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDao.update(Category(id = id, name = name))
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDao.delete(id)
        }
    }
}