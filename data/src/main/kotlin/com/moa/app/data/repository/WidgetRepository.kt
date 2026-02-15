package com.moa.app.data.repository

import com.moa.app.core.model.widget.Widget

interface WidgetRepository {
    suspend fun getWidget(): Widget
}