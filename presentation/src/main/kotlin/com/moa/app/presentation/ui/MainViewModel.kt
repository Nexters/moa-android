package com.moa.app.presentation.ui

import androidx.lifecycle.ViewModel
import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.model.MoaDialogProperties
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val moaSideEffectBus: MoaSideEffectBus,
) : ViewModel() {
    val moaSideEffects = moaSideEffectBus.sideEffects

    private val _dialog = MutableStateFlow<MoaDialogProperties?>(null)
    val dialog = _dialog.asStateFlow()

    fun setDialog(dialogProperties: MoaDialogProperties?) {
        _dialog.value = dialogProperties
    }
}