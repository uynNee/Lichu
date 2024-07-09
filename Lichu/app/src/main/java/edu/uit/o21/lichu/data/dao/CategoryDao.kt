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

//    @Query("SELECT * FROM category WHERE name=:name")
//    suspend fun getOne(name: String): Category?

    @Query("SELECT COUNT(*) > 0 FROM category WHERE name = :categoryName")
    fun checkName(categoryName: String): LiveData<Boolean>

    @Insert
    fun insert(category: Category)

    @Insert
    fun insertGetId(category: Category): Long

    @Update
    fun update(category: Category)

    @Query("DELETE FROM category where id=:id")
    fun delete(id: Int)
}