package com.moa.app.presentation.model

import androidx.navigation3.runtime.NavKey

sealed interface MoaSideEffect {
    @JvmInline
    value class Navigate(val destination: NavKey) : MoaSideEffect

    @JvmInline
    value class Dialog(val dialog: MoaDialogProperties?) : MoaSideEffect

    @JvmInline
    value class Loading(val isLoading: Boolean) : MoaSideEffect

    @JvmInline
    value class Failure(val exception: Throwable) : MoaSideEffect
}