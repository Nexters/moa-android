package com.moa.salary.app.data.di

import com.moa.salary.app.data.repository.AuthRepository
import com.moa.salary.app.data.repository.AuthRepositoryImpl
import com.moa.salary.app.data.repository.CalendarRepository
import com.moa.salary.app.data.repository.CalendarRepositoryImpl
import com.moa.salary.app.data.repository.HomeRepository
import com.moa.salary.app.data.repository.HomeRepositoryImpl
import com.moa.salary.app.data.repository.OnboardingRepository
import com.moa.salary.app.data.repository.OnboardingRepositoryImpl
import com.moa.salary.app.data.repository.SettingRepository
import com.moa.salary.app.data.repository.SettingRepositoryImpl
import com.moa.salary.app.data.repository.TokenRepository
import com.moa.salary.app.data.repository.TokenRepositoryImpl
import com.moa.salary.app.data.repository.WorkdayRepository
import com.moa.salary.app.data.repository.WorkdayRepositoryImpl
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

    @Binds
    @Singleton
    fun bindsTokenRepository(
        impl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    @Singleton
    fun bindsSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    fun bindsAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    fun bindsWorkdayRepository(
        impl: WorkdayRepositoryImpl
    ): WorkdayRepository

    @Binds
    @Singleton
    fun bindsHomeRepository(
        impl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    fun bindsCalendarRepository(
        impl: CalendarRepositoryImpl,
    ): CalendarRepository
}