package com.moa.app.data.di

import com.moa.app.data.repository.OnboardingRepository
import com.moa.app.data.repository.OnboardingRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository
}