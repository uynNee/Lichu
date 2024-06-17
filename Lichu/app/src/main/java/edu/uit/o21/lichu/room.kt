package edu.uit.o21.lichu

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Entity
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val task: String,
    var isDone: Boolean = false
)

@Dao
interface ToDoDao {
    @Query("SELECT * FROM ToDoItem")
    fun getAll(): List<ToDoItem>

    @Insert
    fun insert(item: ToDoItem)

    @Update
    fun update(item: ToDoItem)

    @Delete
    fun delete(item: ToDoItem)
}

@Database(entities = [ToDoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
}

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "lichu-db"
    ).fallbackToDestructiveMigration().build()

    private val dao = db.toDoDao()

    val items = mutableStateListOf<ToDoItem>()

    init {
        loadItems()
    }

    private fun loadItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val itemsFromDb = dao.getAll()
                withContext(Dispatchers.Main) {
                    items.addAll(itemsFromDb)
                }
            }
        }
    }

    fun addItem(task: String) {
        val item = ToDoItem(id = null, task = task)
        items.add(item)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.insert(item)
            }
        }
    }

    fun removeItem(item: ToDoItem) {
        items.remove(item)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.delete(item)
            }
        }
    }

    fun toggleDone(item: ToDoItem) {
        val newItem = item.copy(isDone = !item.isDone)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dao.update(newItem)
            }
            withContext(Dispatchers.Main) {
                val index = items.indexOf(item)
                if (index != -1) {
                    items[index] = newItem
                }
            }
        }
    }
}