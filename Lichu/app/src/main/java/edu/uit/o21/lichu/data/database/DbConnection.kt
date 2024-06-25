package edu.uit.o21.lichu.data.database

import androidx.room.Database
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
    version = 1
)
abstract class DbConnection : RoomDatabase() {
    companion object{
        const val NAME="Lichu_DB"
    }
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getTodoDao(): ToDoDao
}