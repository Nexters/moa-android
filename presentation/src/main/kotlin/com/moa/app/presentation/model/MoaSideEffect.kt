package com.moa.app.presentation.model

import com.moa.app.presentation.navigation.Screen

sealed interface MoaSideEffect {
    @JvmInline
    value class Navigate(val destination: Screen) : MoaSideEffect
}