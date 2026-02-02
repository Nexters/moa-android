package com.moa.app.presentation.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.coroutines.launch

@Composable
fun MoaDialog(
    properties: MoaDialogProperties,
    onDismissRequest: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = properties.cancelable,
            dismissOnClickOutside = properties.cancelable,
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MoaTheme.colors.containerSecondary,
                    shape = RoundedCornerShape(MoaTheme.radius.radius16)
                )
                .padding(
                    horizontal = MoaTheme.spacing.spacing20,
                    vertical = MoaTheme.spacing.spacing24,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            properties.title?.let {
                Text(
                    text = it,
                    style = MoaTheme.typography.t3_700,
                    color = MoaTheme.colors.textHighEmphasis,
                    textAlign = TextAlign.Center,
                )
            }

            properties.message?.let {
                Spacer(Modifier.height(MoaTheme.spacing.spacing8))

                Text(
                    text = it,
                    style = MoaTheme.typography.b2_400,
                    color = MoaTheme.colors.textMediumEmphasis,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            when (properties) {
                is MoaDialogProperties.Alert -> {
                    AlertButtonRow(
                        alertText = properties.alertText,
                        onAlert = {
                            onDismissRequest()
                            scope.launch {
                                properties.onAlert?.invoke()
                            }
                        }
                    )
                }

                is MoaDialogProperties.Confirm -> {
                    ConfirmButtonRow(
                        positiveText = properties.positiveText,
                        negativeText = properties.negativeText,
                        onPositive = {
                            onDismissRequest()
                            scope.launch {
                                properties.onPositive()
                            }
                        },
                        onNegative = {
                            onDismissRequest()
                            scope.launch {
                                properties.onNegative?.invoke()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmButtonRow(
    positiveText: String,
    negativeText: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        MoaTertiaryButton(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            onClick = onNegative,
        ) {
            Text(
                text = negativeText,
                style = MoaTheme.typography.b1_600,
            )
        }

        Spacer(modifier = Modifier.padding(MoaTheme.spacing.spacing12))

        MoaPrimaryButton(
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            onClick = onPositive,
        ) {
            Text(
                text = positiveText,
                style = MoaTheme.typography.b1_600,
            )
        }
    }
}

@Composable
private fun AlertButtonRow(
    alertText: String,
    onAlert: () -> Unit,
) {
    MoaPrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onAlert,
    ) {
        Text(text = alertText)
    }
}

@Stable
sealed interface MoaDialogProperties {
    val title: String?
    val message: String?
    val cancelable: Boolean

    data class Confirm(
        override val title: String?,
        override val message: String? = null,
        override val cancelable: Boolean = true,
        val positiveText: String = "확인",
        val negativeText: String = "취소",
        val onPositive: suspend () -> Unit,
        val onNegative: (suspend () -> Unit)? = null,
    ) : MoaDialogProperties

    data class Alert(
        override val title: String? = null,
        override val message: String? = null,
        override val cancelable: Boolean = true,
        val alertText: String = "확인",
        val onAlert: (suspend () -> Unit)? = null,
    ) : MoaDialogProperties
}

@Preview
@Composable
private fun MoaDialogPreview() {
    MoaTheme {
        Column {
            MoaDialog(
                properties = MoaDialogProperties.Confirm(
                    title = "정말로 삭제하시겠습니까?",
                    message = "삭제된 데이터는 복구할 수 없습니다.",
                    onPositive = {},
                ),
                onDismissRequest = {}
            )
        }
    }
}