package com.moa.app.presentation.extensions

import com.moa.app.presentation.bus.MoaSideEffectBus
import com.moa.app.presentation.manager.ErrorManager
import com.moa.app.presentation.model.MoaSideEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun <T> (suspend () -> T).execute(
    bus: MoaSideEffectBus? = null,
    scope: CoroutineScope,
    onRetry: () -> Unit = {},
    onSuccess: (T) -> Unit,
): Job {
    return scope.launch {
        bus?.emit(MoaSideEffect.Loading(true))

        try {
            onSuccess(this@execute())
        } catch (e: Throwable) {
            val moaException = ErrorManager.map(e)
            bus?.emit(
                MoaSideEffect.Failure(
                    exception = moaException,
                    onRetry = onRetry,
                )
            )
        } finally {
            bus?.emit(MoaSideEffect.Loading(false))
        }
    }
}

