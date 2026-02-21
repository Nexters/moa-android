package com.moa.app.presentation.ui.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.moa.app.presentation.ui.widget.worker.WidgetWorkManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoaWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MoaWidget()

    @Inject
    lateinit var widgetWorkManager: WidgetWorkManager

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        widgetWorkManager.schedulePeriodicUpdate()
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        widgetWorkManager.cancelPeriodicUpdate()
    }
}