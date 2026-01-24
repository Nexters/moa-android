package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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