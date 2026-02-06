package com.moa.app.data.di

import android.content.Context
import com.moa.app.data.local.TokenDataStore
import com.moa.app.data.security.TinkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 보안 관련 의존성 주입 모듈
 *
 * TinkManager와 TokenDataStore를 싱글톤으로 제공
 */
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
