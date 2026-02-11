package com.moa.app.presentation.ui.widget

import com.moa.app.core.model.widget.Widget
import com.moa.app.data.repository.WidgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoaWidgetViewModel @Inject constructor(
    private val widgetRepository: WidgetRepository
) {
    fun getWidgetFlow(): Flow<Widget> = flow {
        emit(widgetRepository.getWidget())
    }
}