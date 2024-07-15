package edu.uit.o21.lichu.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import edu.uit.o21.lichu.MainApplication
import edu.uit.o21.lichu.ui.NotificationService
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DeadlineWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return try {
            checkDeadlines()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun checkDeadlines() {
        val toDoDao = MainApplication.dbConnection.getTodoDao()
        val notificationService = NotificationService(applicationContext)
        val today = LocalDate.now()
        runBlocking {
            val todos = toDoDao.getAllToDo()
            todos.forEach { todo ->
                val deadline = todo.endTime
                val task = todo.content
                if (!todo.isDone && deadline != null) {
                    val daysUntilDeadline = ChronoUnit.DAYS.between(today, deadline)
                    val message = when (daysUntilDeadline) {
                        0L -> "Task $task is due today!"
                        1L -> "Task $task is due tomorrow!"
                        2L -> "Task $task is due in 2 days!"
                        3L -> "Task $task is due in 3 days!"
                        else -> null
                    }
                    message?.let {
                        notificationService.showNotification(it, todo.id)
                    }
                }
            }
        }
    }
}