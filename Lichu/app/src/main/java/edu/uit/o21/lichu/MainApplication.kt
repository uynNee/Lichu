package edu.uit.o21.lichu

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import edu.uit.o21.lichu.data.database.DbConnection
import edu.uit.o21.lichu.ui.NOTIFICATION_CHANNEL_ID
import edu.uit.o21.lichu.ui.NOTIFICATION_CHANNEL_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class MainApplication : Application() {
    companion object {
        lateinit var dbConnection: DbConnection
        lateinit var appContext: Context
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
        runBlocking(Dispatchers.IO) {
            dbConnection = Room.databaseBuilder(
                applicationContext,
                DbConnection::class.java,
                DbConnection.NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}