package com.moa.app.core.model

import androidx.compose.runtime.Stable

@Stable
sealed class Term(
    open val title: String,
    open val url: String,
    open val checked: Boolean,
) {
    data class All(
        override val title: String,
        override val url: String = "",
        override val checked: Boolean,
    ) : Term(
        title = title,
        url = url,
        checked = checked,
    )

    data class Required(
        override val title: String,
        override val url: String,
        override val checked: Boolean,
    ) : Term(
        title = title,
        url = url,
        checked = checked,
    )

    data class Optional(
        override val title: String,
        override val url: String,
        override val checked: Boolean,
    ) : Term(
        title = title,
        url = url,
        checked = checked,
    )
}