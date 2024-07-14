package edu.uit.o21.lichu.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import edu.uit.o21.lichu.ui.NotificationService
import edu.uit.o21.lichu.viewmodel.ToDoViewModel
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class DeadlineWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return try {
            checkDeadlines()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun checkDeadlines() {
        val toDo = ToDoViewModel()
        val notificationService = NotificationService(applicationContext)
        runBlocking {
            val todos = toDo.getAll()
            val today = LocalDate.now()
            todos.forEach { todo ->
                val deadline = todo.endTime
                val task = todo.content
                if (!todo.isDone && deadline != null) {
                    val message = when {
                        deadline.isEqual(today) -> "Task $task is due today!"
                        deadline.minusDays(1).isEqual(today) -> "Deadline $task is tomorrow!"
                        deadline.minusDays(3).isEqual(today) -> "Deadline $task is in 3 days!"
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