package com.moa.app.presentation.model

import androidx.compose.runtime.Stable

@Stable
sealed interface MoaDialogProperties {
    val title: String?
    val message: String?
    val cancelable: Boolean

    data class Confirm(
        override val title: String?,
        override val message: String? = null,
        override val cancelable: Boolean = true,
        val positiveText: String = "확인",
        val negativeText: String = "취소",
        val onPositive: suspend () -> Unit,
        val onNegative: (suspend () -> Unit)? = null,
    ) : MoaDialogProperties

    data class Alert(
        override val title: String? = null,
        override val message: String? = null,
        override val cancelable: Boolean = true,
        val alertText: String = "확인",
        val onAlert: (suspend () -> Unit)? = null,
    ) : MoaDialogProperties
}