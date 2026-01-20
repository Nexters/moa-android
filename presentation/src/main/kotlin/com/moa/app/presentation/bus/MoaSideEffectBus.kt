package com.moa.app.presentation.bus

import com.moa.app.presentation.model.MoaSideEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoaSideEffectBus @Inject constructor() {
    private val _sideEffects = MutableSharedFlow<MoaSideEffect>()
    val sideEffects = _sideEffects.asSharedFlow()

    suspend fun emit(sideEffect: MoaSideEffect) {
        _sideEffects.emit(sideEffect)
    }
}