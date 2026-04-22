package com.moa.salary.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.kakao.sdk.common.KakaoSdk
import com.moa.salary.app.presentation.ui.widget.worker.WidgetWorkManager
import com.posthog.android.PostHogAndroid
import com.posthog.android.PostHogAndroidConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MoaApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var widgetWorkManager: WidgetWorkManager

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        initSdk()
        createNotificationChannel()
    }

    private fun initSdk() {
        val config = PostHogAndroidConfig(
            apiKey = BuildConfig.POSTHOG_KEY,
            host = BuildConfig.POSTHOG_URL,
        )
        PostHogAndroid.setup(this, config)

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "moaChannelId",
            "Moa",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}