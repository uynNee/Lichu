package edu.uit.o21.lichu

import android.app.Application
import androidx.room.Room
import edu.uit.o21.lichu.data.database.DbConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainApplication :Application(){
    companion object{
        lateinit var dbConnection:DbConnection
    }
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            dbConnection = Room.databaseBuilder(
                applicationContext,
                DbConnection::class.java,
                DbConnection.NAME
            )
                .fallbackToDestructiveMigration() // Xóa database cũ nếu có
                .build()
        }
    }
}