package com.moa.salary.app.data.di

import android.content.Context
import com.moa.salary.app.data.local.TokenDataStore
import com.moa.salary.app.data.security.TinkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideTinkManager(
        @ApplicationContext context: Context
    ): TinkManager {
        return TinkManager(context)
    }

    @Provides
    @Singleton
    fun provideTokenDataStore(
        @ApplicationContext context: Context,
        tinkManager: TinkManager
    ): TokenDataStore {
        return TokenDataStore(context, tinkManager)
    }
}
