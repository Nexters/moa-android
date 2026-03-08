package com.moa.salary.app.data.di

import com.moa.salary.app.core.util.Constants.TIMEOUT_SECONDS
import com.moa.salary.app.data.BuildConfig
import com.moa.salary.app.data.remote.api.MoaService
import com.moa.salary.app.data.remote.api.OnboardingService
import com.moa.salary.app.data.remote.api.SettingService
import com.moa.salary.app.data.remote.api.WorkdayService
import com.moa.salary.app.data.remote.api.AuthService
import com.moa.salary.app.data.remote.converter.ContentOnlyConverterFactory
import com.moa.salary.app.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
            encodeDefaults = true
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json; charset=utf-8".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ContentOnlyConverterFactory(json))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideOnboardingService(retrofit: Retrofit): OnboardingService =
        retrofit.create(OnboardingService::class.java)

    @Provides
    @Singleton
    fun provideMoaService(retrofit: Retrofit): MoaService =
        retrofit.create(MoaService::class.java)

    @Provides
    @Singleton
    fun provideSettingService(retrofit: Retrofit): SettingService =
        retrofit.create(SettingService::class.java)

    @Provides
    @Singleton
    fun provideWorkdayService(retrofit: Retrofit): WorkdayService =
        retrofit.create(WorkdayService::class.java)
}
