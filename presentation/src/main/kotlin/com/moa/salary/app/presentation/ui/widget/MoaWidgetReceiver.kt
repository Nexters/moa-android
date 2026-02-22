package com.moa.salary.app.presentation.ui.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import dagger.hilt.android.EntryPointAccessors

class MoaWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MoaWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            MoaWidgetEntryPoint::class.java
        )
        entryPoint.getWidgetWorkManager().schedulePeriodicUpdate()
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        val entryPoint = EntryPointAccessors.fromApplication(
            context,
            MoaWidgetEntryPoint::class.java
        )
        entryPoint.getWidgetWorkManager().schedulePeriodicUpdate()
    }
}