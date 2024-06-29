package edu.uit.o21.lichu

import android.app.Application
import androidx.room.Room
import edu.uit.o21.lichu.data.database.DbConnection

class MainApplication :Application(){
    companion object{
        lateinit var dbConnection:DbConnection
    }
    override fun onCreate() {
        super.onCreate()
        dbConnection= Room.databaseBuilder(
            applicationContext,
            DbConnection::class.java,
            DbConnection.NAME
        ).build()
    }
}