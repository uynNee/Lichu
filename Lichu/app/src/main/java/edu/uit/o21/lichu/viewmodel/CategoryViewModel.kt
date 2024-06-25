package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.data.entity.Category

class CategoryViewModel:ViewModel(){
    val categoryDao=MainApplication.dbConnection.getCategoryDao()

    val categoryList: LiveData<List<Category>> = categoryDao.getAll()

    fun addCategory(name:String){
        categoryDao.insert(Category(name=name))
    }

    fun updateCategory(name:String){
        categoryDao.update(Category(name=name))
    }

    fun deleteCategory(id:Int){
        categoryDao.delete(id)
    }
}