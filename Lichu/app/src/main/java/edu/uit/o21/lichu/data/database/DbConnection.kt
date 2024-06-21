package edu.uit.o21.lichu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.uit.o21.lichu.data.DateConverter
import edu.uit.o21.lichu.data.dao.CategoryDao
import edu.uit.o21.lichu.data.dao.ToDoDao
import edu.uit.o21.lichu.data.entity.Category
import edu.uit.o21.lichu.data.entity.ToDo

@TypeConverters(value = [DateConverter::class])
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
        fun getDatabase(context: Context): DbConnection {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbConnection::class.java,
                    "lichu_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}