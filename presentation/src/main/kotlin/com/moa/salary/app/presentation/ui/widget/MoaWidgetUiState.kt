package com.moa.salary.app.presentation.ui.widget

import androidx.compose.runtime.Stable
import com.moa.salary.app.core.model.widget.Widget

@Stable
sealed interface MoaWidgetUiState {
    data object Loading : MoaWidgetUiState

    data class Success(val widget: Widget) : MoaWidgetUiState

    data class Error(val message: String) : MoaWidgetUiState
}