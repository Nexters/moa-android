package com.moa.salary.app.presentation.ui.widget.util

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.moa.salary.app.presentation.ui.widget.MoaWidget
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WidgetUpdateManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun updateAllWidgets() {
        try {
            val manager = GlanceAppWidgetManager(context)
            val glanceIds = manager.getGlanceIds(MoaWidget::class.java)

            glanceIds.forEach { glanceId ->
                MoaWidget().update(context, glanceId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun hasActiveWidgets(): Boolean {
        return try {
            val manager = GlanceAppWidgetManager(context)
            val widgets = manager.getGlanceIds(MoaWidget::class.java)
            widgets.isNotEmpty()
        } catch (_: Exception) {
            false
        }
    }
}
