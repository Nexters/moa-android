package com.moa.app.presentation.designsystem.transformation

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert

class CurrencyOutputTransformation : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        val original = asCharSequence().toString()
        if (original.isEmpty()) return

        original.toLongOrNull() ?: return

        val len = original.length
        for (i in len - 3 downTo 1 step 3) {
            insert(i, ",")
        }
    }
}