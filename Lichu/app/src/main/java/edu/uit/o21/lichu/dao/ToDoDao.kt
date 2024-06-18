package edu.uit.o21.lichu.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.uit.o21.lichu.entity.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo WHERE categoryId = :categoryId")
    fun getAll(categoryId:Int): Flow<List<ToDo>>

    @Insert
    suspend fun insert(todo: ToDo)

    @Update
    suspend fun update(todo: ToDo)

    @Delete
    suspend fun delete(todo: ToDo)

    @Query("SELECT DISTINCT categoryId FROM todo WHERE content LIKE :content")
    fun findByContent(content: String): Flow<List<Int>>
}