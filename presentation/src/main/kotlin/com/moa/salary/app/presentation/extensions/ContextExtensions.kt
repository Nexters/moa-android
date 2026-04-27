package com.moa.salary.app.presentation.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

fun Context.openWebBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    startActivity(intent)
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