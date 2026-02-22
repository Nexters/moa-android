package com.moa.app.presentation.ui.widget

import com.moa.app.data.repository.HomeRepository
import com.moa.app.data.repository.WidgetRepository
import com.moa.app.domain.usecase.CalculateAccumulatedSalaryUseCase
import com.moa.app.presentation.usecase.DetermineHomeNavigationUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MoaWidgetEntryPoint {
    fun getWidgetRepository(): WidgetRepository
    fun getHomeRepository(): HomeRepository
    fun getCalculateAccumulatedSalaryUseCase(): CalculateAccumulatedSalaryUseCase
    fun getDetermineHomeNavigationUseCase(): DetermineHomeNavigationUseCase
}