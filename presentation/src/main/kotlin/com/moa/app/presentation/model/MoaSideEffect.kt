package com.moa.app.presentation.model

sealed interface MoaSideEffect {
    @JvmInline
    value class Navigate(val destination: RootNavigation) : MoaSideEffect

    @JvmInline
    value class Dialog(val dialog: MoaDialogProperties?) : MoaSideEffect

    @JvmInline
    value class Loading(val isLoading: Boolean) : MoaSideEffect

    @JvmInline
    value class Failure(val exception: Throwable) : MoaSideEffect
}