package com.moa.app.presentation.ui.widget

import com.moa.app.core.model.widget.Widget
import kotlinx.serialization.Serializable

@Serializable
sealed interface WidgetUiState {
    @Serializable
    data object Loading : WidgetUiState

    @Serializable
    data class Success(val widget: Widget) : WidgetUiState

    @Serializable
    data class Error(val message: String) : WidgetUiState
}