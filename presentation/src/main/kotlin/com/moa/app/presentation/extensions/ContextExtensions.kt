package com.moa.app.presentation.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.sendEmail(appVersion: String) {
    val email = "moa.mymoney@gmail.com"
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