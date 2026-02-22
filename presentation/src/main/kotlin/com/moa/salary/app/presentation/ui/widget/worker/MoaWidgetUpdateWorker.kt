package com.moa.salary.app.presentation.ui.widget.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.moa.salary.app.presentation.ui.widget.util.WidgetUpdateManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MoaWidgetUpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val widgetUpdateManager: WidgetUpdateManager,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            if (!widgetUpdateManager.hasActiveWidgets()) {
                return Result.success()
            }

            widgetUpdateManager.updateAllWidgets()
            Result.success()
        } catch (_: Exception) {
            if (runAttemptCount < MAX_RETRY_ATTEMPTS) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        const val WORK_NAME = "moa_widget_periodic_update"
        private const val MAX_RETRY_ATTEMPTS = 3
    }
}
