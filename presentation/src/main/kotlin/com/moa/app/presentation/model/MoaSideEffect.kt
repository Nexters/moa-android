package com.moa.app.presentation.model

import com.moa.app.presentation.navigation.RootNavigation

sealed interface MoaSideEffect {
    @JvmInline
    value class Navigate(val destination: RootNavigation) : MoaSideEffect

    @JvmInline
    value class Dialog(val dialog: MoaDialogProperties?) : MoaSideEffect
}