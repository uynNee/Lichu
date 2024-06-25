package edu.uit.o21.lichu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.uit.o21.lichu.data.entity.ToDo

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo WHERE categoryId = :categoryId")
    fun getAll(categoryId:Int): LiveData<List<ToDo>>

    @Insert
    fun insert(todo: ToDo)

    @Update
    fun update(todo: ToDo)

    @Query("DELETE FROM todo WHERE id=:id")
    fun delete(id: Int)
//
//    @Query("SELECT DISTINCT categoryId FROM todo WHERE content LIKE :content")
//    fun findByContent(content: String): Flow<List<Int>>
}