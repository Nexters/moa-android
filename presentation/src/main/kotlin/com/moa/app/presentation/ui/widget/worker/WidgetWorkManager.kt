package com.moa.app.presentation.ui.widget.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetWorkManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val workManager = WorkManager.getInstance(context)

    fun schedulePeriodicUpdate() {
        val workRequest = PeriodicWorkRequestBuilder<MoaWidgetUpdateWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setInitialDelay(15, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniquePeriodicWork(
            MoaWidgetUpdateWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelPeriodicUpdate() {
        workManager.cancelUniqueWork(MoaWidgetUpdateWorker.WORK_NAME)
    }
}
