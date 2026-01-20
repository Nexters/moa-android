package com.moa.app.presentation.designsystem.transformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

private const val MAX_AMOUNT = 9_999_999_999L

class SalaryInputTransformation : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val filtered = asCharSequence().filter { it.isDigit() }.toString()

        val longValue = filtered.toLongOrNull() ?: 0L
        if (longValue > MAX_AMOUNT) {
            revertAllChanges()
            return
        }

        if (filtered != asCharSequence().toString()) {
            replace(0, length, filtered)
        }
    }
}