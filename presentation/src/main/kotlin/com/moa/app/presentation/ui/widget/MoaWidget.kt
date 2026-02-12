package com.moa.app.presentation.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.Text
import com.moa.app.presentation.designsystem.theme.MoaTheme
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

class MoaWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val scope = rememberCoroutineScope()
            var uiState by rememberSaveable { mutableStateOf<MoaWidgetUiState>(MoaWidgetUiState.Loading) }
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                MoaWidgetEntryPoint::class.java
            )
            val repository = entryPoint.getWidgetRepository()

            suspend fun getWidgetUiState() {
                uiState = MoaWidgetUiState.Loading
                try {
                    val widget = repository.getWidget()
                    uiState = MoaWidgetUiState.Success(widget)
                } catch (e: Exception) {
                    uiState = MoaWidgetUiState.Error(e.message ?: "알 수 없는 에러")
                }
            }

            LaunchedEffect(Unit) {
                getWidgetUiState()
            }

            MoaTheme {
                MoaWidgetContent(
                    uiState = uiState,
                    onRefresh = {
                        scope.launch {
                            getWidgetUiState()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MoaWidgetContent(
    uiState: MoaWidgetUiState,
    onRefresh: () -> Unit,
) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()

            .clickable { onRefresh() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (uiState) {
            is MoaWidgetUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text("로딩 중...")
            }

            is MoaWidgetUiState.Success -> {
                Text("성공: ${uiState.widget}")
            }

            is MoaWidgetUiState.Error -> {
                Text("에러: ${uiState.message}")
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text("탭하여 재시도")
            }
        }
    }
}
