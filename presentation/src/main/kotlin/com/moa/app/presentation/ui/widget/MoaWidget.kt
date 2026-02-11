package com.moa.app.presentation.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.moa.app.presentation.designsystem.theme.MoaTheme
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class MoaWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact
    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    companion object {
        val stateKey = stringPreferencesKey("widget_state")

        val json = Json {
            encodeDefaults = true
            classDiscriminator = "type"
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        updateAppWidgetState(context, id) { prefs ->
            if (!prefs.contains(stateKey)) {
                prefs[stateKey] = json.encodeToString<WidgetUiState>(WidgetUiState.Loading)

                try {
                    val entryPoint = EntryPointAccessors.fromApplication(
                        context.applicationContext,
                        MoaWidgetEntryPoint::class.java
                    )
                    val repository = entryPoint.getWidgetRepository()
                    val widget = runBlocking { repository.getWidget() }

                    prefs[stateKey] = json.encodeToString<WidgetUiState>(
                        WidgetUiState.Success(widget)
                    )
                } catch (e: Exception) {
                    prefs[stateKey] = json.encodeToString<WidgetUiState>(
                        WidgetUiState.Error(e.message ?: "초기 로드 실패")
                    )
                }
            }
        }

        provideContent {
            MoaTheme {
                val stateJson = currentState(stateKey)
                    ?: json.encodeToString<WidgetUiState>(WidgetUiState.Loading)
                val uiState = try {
                    json.decodeFromString<WidgetUiState>(stateJson)
                } catch (e: Exception) {
                    e.printStackTrace()
                    WidgetUiState.Error("상태 파싱 실패")
                }

                MoaWidgetContent(uiState = uiState)
            }
        }
    }
}

@Composable
fun MoaWidgetContent(uiState: WidgetUiState) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()

            .clickable(actionRunCallback<RefreshActionCallback>()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (uiState) {
            is WidgetUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text("로딩 중...")
            }

            is WidgetUiState.Success -> {
                Text("성공: ${uiState.widget}")
            }

            is WidgetUiState.Error -> {
                Text("에러: ${uiState.message}")
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text("탭하여 재시도")
            }
        }
    }
}
