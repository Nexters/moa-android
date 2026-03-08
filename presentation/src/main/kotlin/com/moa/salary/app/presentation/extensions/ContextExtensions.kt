package com.moa.salary.app.presentation.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

fun Context.sendEmail(appVersion: String) {
    val email = "moa.salary@gmail.com"
    val subject = "[문의] 모아 서비스에 문의드립니다."
    val body = """
        문의 유형: (버그 신고 / 제휴·광고 / 계정·결제 / 신고 / 기능 제안 / 기타)
        상세 설명:

        스크린샷/영상(선택):

        ------------------------------
        앱 버전/빌드: $appVersion
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        this.startActivity(intent)
    } catch (_: ActivityNotFoundException) {

    }
}

fun Context.openNotificationSettings() {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
    }
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

fun Context.openPlayStore() {
    try {
        val intent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {

    }
}