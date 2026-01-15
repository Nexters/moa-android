package com.moa.app.presentation.ui

import androidx.lifecycle.ViewModel
import com.moa.app.presentation.bus.MoaSideEffectBus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects
}