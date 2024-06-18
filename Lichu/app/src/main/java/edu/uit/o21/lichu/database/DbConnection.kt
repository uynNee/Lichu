package edu.uit.o21.lichu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.uit.o21.lichu.dao.CategoryDao
import edu.uit.o21.lichu.dao.ToDoDao
import edu.uit.o21.lichu.entity.Category
import edu.uit.o21.lichu.entity.ToDo

@Database(
    entities = [Category::class, ToDo::class],
    version = 1,
    exportSchema = false
)
abstract class DbConnection : RoomDatabase(){
    abstract fun categoryDao(): CategoryDao
    abstract fun toDoDao(): ToDoDao

    companion object{
        @Volatile
        var INSTANCE: DbConnection? = null
        fun getInstance(context: Context): DbConnection{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    DbConnection::class.java,
                    "lichu_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}