package com.moa.app.presentation.ui.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import com.moa.app.presentation.designsystem.theme.MoaTheme
import dagger.hilt.EntryPoints

class MoaWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val viewModel = EntryPoints.get(
            appContext,
            MoaWidgetEntryPoint::class.java
        ).getMoaWidgetViewModel()

        provideContent {
            MoaTheme {
                val widget by viewModel.getWidgetFlow().collectAsState(null)
                Log.e("ABC", widget.toString())

                MoaWidgetContent()
            }
        }
    }
}

@Composable
fun MoaWidgetContent(

) {
    Column(
        modifier = GlanceModifier.fillMaxSize().background(MoaTheme.colors.bgPrimary)
    ) {

    }
}