package com.moa.salary.app.presentation.ui.widget

import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.presentation.ui.widget.worker.WidgetWorkManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MoaWidgetEntryPoint {
    fun getWidgetWorkManager(): WidgetWorkManager
    fun getHomeRepository(): HomeRepository
}