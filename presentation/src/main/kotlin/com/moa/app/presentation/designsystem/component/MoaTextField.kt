package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import com.moa.app.core.util.PayrollConstants
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaFilledTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String,
    trailingText: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(MoaTheme.radius.radius12))
            .background(
                color = MoaTheme.colors.containerPrimary,
                shape = RoundedCornerShape(MoaTheme.radius.radius12),
            )
            .padding(
                horizontal = MoaTheme.spacing.spacing20,
                vertical = MoaTheme.spacing.spacing16
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier = Modifier.weight(1f),
            state = state,
            inputTransformation = inputTransformation,
            outputTransformation = outputTransformation,
            cursorBrush = SolidColor(Color.White),
            keyboardOptions = keyboardOptions,
            textStyle = MoaTheme.typography.t2_700.copy(
                color = MoaTheme.colors.textHighEmphasis
            ),
            decorator = { innerTextField ->
                if (state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MoaTheme.colors.textDisabled,
                        style = MoaTheme.typography.t2_700,
                    )
                }
                innerTextField()
            }
        )

        Spacer(Modifier.width(10.dp))

        Text(
            text = trailingText,
            color = MoaTheme.colors.textMediumEmphasis,
            style = MoaTheme.typography.b1_400,
        )
    }

}

@Preview(backgroundColor = 0xFF000000, showBackground = true)
@Composable
private fun MoaTextFieldPreview() {
    MoaTheme {
        MoaFilledTextField(
            modifier = Modifier.fillMaxWidth(),
            state = TextFieldState(),
            placeholder = "0",
            trailingText = "원"
        )
    }
}

class CurrencyInputTransformation(
    private val maxAmount: Long = PayrollConstants.MAX_SALARY_AMOUNT
) : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        val filtered = asCharSequence().filter { it.isDigit() }.toString()
        val longValue = filtered.toLongOrNull() ?: 0L

        if (longValue > maxAmount) {
            revertAllChanges()
            return
        }

        if (filtered != asCharSequence().toString()) {
            replace(0, length, filtered)
        }
    }
}

class CurrencyOutputTransformation : OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        val original = asCharSequence().toString()
        if (original.isEmpty() || original.toLongOrNull() == null) return

        val len = original.length
        for (i in len - 3 downTo 1 step 3) {
            insert(i, ",")
        }
    }
}