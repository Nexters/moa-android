package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun MoaTextFieldWithDescription(
    modifier: Modifier = Modifier,
    description1: String,
    description2: String,
    state: TextFieldState,
    placeholder: String,
    inputTransformation: InputTransformation? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = description1,
            color = MoaTheme.colors.textHighEmphasis,
            style = MoaTheme.typography.t1_700,
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    color = MoaTheme.colors.containerPrimary,
                    shape = RoundedCornerShape(MoaTheme.radius.radius16),
                ),
            contentAlignment = Alignment.Center,
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoaTheme.spacing.spacing20),
                state = state,
                textStyle = MoaTheme.typography.h3_700.copy(
                    color = MoaTheme.colors.textGreen,
                    textAlign = TextAlign.Center,
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                cursorBrush = SolidColor(Color.White),
                inputTransformation = inputTransformation,
                decorator = { innerTextField ->
                    if (state.text.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = placeholder,
                            style = MoaTheme.typography.h3_700,
                            color = MoaTheme.colors.textDisabled,
                            textAlign = TextAlign.Center,
                        )
                    }
                    innerTextField()
                }
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = description2,
            color = MoaTheme.colors.textHighEmphasis,
            style = MoaTheme.typography.t1_700,
        )
    }
}

@Composable
fun MoaFilledTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String,
    trailingText: String,
    keyboardOptions : KeyboardOptions = KeyboardOptions.Default,
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
private fun MoaTextFieldWithDescriptionPreview() {
    MoaTheme {
        MoaTextFieldWithDescription(
            description1 = "닉네임",
            description2 = "로 가입할래요",
            state = TextFieldState(),
            placeholder = "닉네임을 입력해주세요",
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