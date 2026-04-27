package com.moa.salary.app.presentation.model

import com.posthog.PostHog

interface PosthogEvent {
    val event: String
    val properties: Map<String, Any>?
        get() = null

    fun sendEvent() {
        PostHog.capture(
            event = event,
            properties = properties,
        )
    }
}