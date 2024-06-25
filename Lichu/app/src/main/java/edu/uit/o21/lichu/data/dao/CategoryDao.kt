package edu.uit.o21.lichu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.uit.o21.lichu.data.entity.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll(): LiveData<List<Category>>

    @Insert
    fun insert(category: Category)

    @Update
    fun update(category: Category)

    @Query("DELETE FROM category where id=:id")
    fun delete(id:Int)
//
//    @Query("SELECT * FROM category WHERE name LIKE :name")
//    fun findByName(name: String): Flow<List<Category>>
//
//    @Query("SELECT * FROM category WHERE id IN (:ids)")
//    fun findById(ids: List<Int>): Flow<List<Category>>
}