package com.moa.app.presentation.ui.widget

import androidx.compose.runtime.Stable
import com.moa.app.core.model.widget.Widget

@Stable
sealed interface MoaWidgetUiState {
    data object Loading : MoaWidgetUiState

    data class Success(val widget: Widget) : MoaWidgetUiState

    data class Error(val message: String) : MoaWidgetUiState
}