package edu.uit.o21.lichu.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import edu.uit.o21.lichu.ui.theme.LichuTheme
import edu.uit.o21.lichu.worker.DeadlineWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LichuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Lichu()
                    RequestNotificationPermission()
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @SuppressLint("InlinedApi")
    @Composable
    fun RequestNotificationPermission() {
        val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    scheduleDailyWorker()
                }
            }

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !permissionState.status.isGranted) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                scheduleDailyWorker()
            }
        }
    }

    private fun scheduleDailyWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DeadlineWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DeadlineWorker",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWorkRequest
        )
    }
}