package edu.uit.o21.lichu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.uit.o21.lichu.data.Repository
import edu.uit.o21.lichu.data.entity.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoryViewModel (private val resository: Repository) : ViewModel(){
    fun getAllCategories(): Flow<List<Category>> = resository.getCategories
    fun getCategoriesById(ids: List<Int>): Flow<List<Category>> = resository.getCategoriesById(ids)
    fun getCategoriesByName(name: String): Flow<List<Category>> = resository.getCategoriesByName(name)

    fun insert(category: Category) = viewModelScope.launch {
        resository.insertCategory(category)
    }
    fun delete(category: Category) = viewModelScope.launch {
        resository.deleteCategory(category)
    }
    fun update(category: Category) = viewModelScope.launch {
        resository.updateCategory(category)
    }
}
class CategoryViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}