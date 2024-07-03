package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
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

    fun checkName(name: String):LiveData<Boolean>{
        return categoryDao.checkName(name)
    }

    fun addCategory(name:String){
        viewModelScope.launch (Dispatchers.IO){
            categoryDao.insert(Category(name=name))
        }
    }

    fun updateCategory(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDao.update(Category(name = name))
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDao.delete(id)
        }
    }
}