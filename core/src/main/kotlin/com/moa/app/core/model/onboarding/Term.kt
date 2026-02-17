package com.moa.app.core.model.onboarding

import androidx.compose.runtime.Stable

@Stable
sealed class Term(
    open val code: String,
    open val title: String,
    open val url: String,
    open val checked: Boolean,
    val order: Int,
) {
    data class All(
        override val code: String = "",
        override val title: String,
        override val url: String = "",
        override val checked: Boolean,
    ) : Term(
        code = code,
        title = title,
        url = url,
        checked = checked,
        order = 0,
    )

    data class Required(
        override val code: String,
        override val title: String,
        override val url: String,
        override val checked: Boolean,
    ) : Term(
        code = code,
        title = title,
        url = url,
        checked = checked,
        order = 1,
    )

    data class Optional(
        override val code: String,
        override val title: String,
        override val url: String,
        override val checked: Boolean,
    ) : Term(
        code = code,
        title = title,
        url = url,
        checked = checked,
        order = 2,
    )
}