package com.moa.app.presentation.ui.setting.salarydate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.designsystem.component.MoaBottomSheet
import com.moa.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.app.presentation.designsystem.component.MoaWheelPicker
import com.moa.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryDateBottomSheet(
    salaryDate: Int,
    onConfirm: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var date by remember { mutableIntStateOf(salaryDate) }

    MoaBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoaTheme.spacing.spacing20)
        ) {
            Text(
                text = "월급일을 선택해주세요",
                style = MoaTheme.typography.t1_700,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            SalaryDateWheelPicker(
                date = date,
                onDateChange = { date = it }
            )

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24
                    )
                    .height(64.dp),
                onClick = {
                    onConfirm(date)
                    onDismissRequest()
                },
            ) {
                Text(
                    text = "확인",
                    style = MoaTheme.typography.t3_700,
                )
            }
        }
    }
}

@Composable
private fun SalaryDateWheelPicker(
    date: Int,
    onDateChange: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(236.dp)
            .clip(RoundedCornerShape(MoaTheme.radius.radius16))
            .background(
                color = MoaTheme.colors.containerSecondary,
                shape = RoundedCornerShape(MoaTheme.radius.radius16),
            )
            .padding(MoaTheme.spacing.spacing8),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .background(
                    color = MoaTheme.colors.containerPrimary,
                    shape = RoundedCornerShape(MoaTheme.radius.radius12)
                )
        )

        MoaWheelPicker(
            modifier = Modifier.width(120.dp),
            items = (1..28).toList().toImmutableList(),
            initialSelectedIndex = date - 1,
            onItemSelected = onDateChange,
            itemToString = { "${it}일" }
        )
    }
}