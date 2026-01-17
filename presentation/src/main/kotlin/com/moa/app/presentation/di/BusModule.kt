package com.moa.app.presentation.di

import com.moa.app.presentation.bus.MoaSideEffectBus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BusModule {
    @Provides
    @Singleton
    fun provideMoaSideEffectBus() = MoaSideEffectBus()
}