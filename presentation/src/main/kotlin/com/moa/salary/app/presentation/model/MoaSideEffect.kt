package com.moa.salary.app.presentation.model

sealed interface MoaSideEffect {
    @JvmInline
    value class Toast(val message: String) : MoaSideEffect

    @JvmInline
    value class Navigate(val destination: RootNavigation) : MoaSideEffect

    @JvmInline
    value class Dialog(val dialog: MoaDialogProperties?) : MoaSideEffect

    @JvmInline
    value class Loading(val isLoading: Boolean) : MoaSideEffect

    data class Failure(
        val exception: Throwable,
        val onRetry: () -> Unit,
    ) : MoaSideEffect
}