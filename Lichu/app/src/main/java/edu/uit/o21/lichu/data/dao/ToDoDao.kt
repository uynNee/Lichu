package edu.uit.o21.lichu.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.uit.o21.lichu.data.entity.ToDo
import java.time.LocalDate

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo")
    fun calendarGetAll(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo WHERE categoryId = :categoryId ORDER BY endTime ASC")
    fun getAll(categoryId:Int): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo WHERE id=:todoId")
    fun getToDoById(todoId:Int): LiveData<ToDo>

    @Insert
    fun insert(todo: ToDo)

    @Query("UPDATE ToDo SET content = :content, endTime = :endTime WHERE id = :id")
    fun update(id: Int, content: String, endTime: LocalDate?)

    @Query("UPDATE ToDo SET isDone = :isDone where id=:id")
    fun updateIsDone(id:Int, isDone:Boolean)

    @Query("DELETE FROM todo WHERE id=:id")
    fun delete(id: Int)
}