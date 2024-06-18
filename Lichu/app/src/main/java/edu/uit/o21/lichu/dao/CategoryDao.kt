package edu.uit.o21.lichu.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.uit.o21.lichu.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll(): Flow<List<Category>>

    @Insert
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Query("SELECT * FROM category WHERE name LIKE :name")
    fun findByName(name: String): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE categoryId IN (:categoryIds)")
    fun findById(categoryIds: List<Int>): Flow<List<Category>>
}