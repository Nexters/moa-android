package com.moa.salary.app.presentation.ui.setting.salaryday

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.moa.salary.app.presentation.R
import com.moa.salary.app.presentation.designsystem.component.MoaBottomSheet
import com.moa.salary.app.presentation.designsystem.component.MoaPrimaryButton
import com.moa.salary.app.presentation.designsystem.component.MoaWheelPicker
import com.moa.salary.app.presentation.designsystem.theme.MoaTheme
import kotlinx.collections.immutable.toImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryDayBottomSheet(
    salaryDay: Int,
    onConfirm: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var day by remember { mutableIntStateOf(salaryDay) }
    val salaryInfoText = when (day) {
        29, 30 -> "해당 날짜가 없는 달에는 말일이 월급일로 설정돼요"
        31 -> "매 달 말일을 월급일로 설정할게요"
        else -> null
    }

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

            AnimatedVisibility(
                modifier = Modifier.padding(top = MoaTheme.spacing.spacing4),
                visible = salaryInfoText != null
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_16_warning),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = MoaTheme.colors.textGreen)
                    )

                    Spacer(Modifier.width(MoaTheme.spacing.spacing4))

                    Text(
                        text = salaryInfoText ?: "",
                        color = MoaTheme.colors.textGreen,
                        style = MoaTheme.typography.b2_500,
                    )
                }
            }

            Spacer(Modifier.height(MoaTheme.spacing.spacing16))

            SalaryDayWheelPicker(
                day = day,
                onDayChange = { day = it }
            )

            MoaPrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = MoaTheme.spacing.spacing20,
                        bottom = MoaTheme.spacing.spacing24
                    ),
                onClick = {
                    onConfirm(day)
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
private fun SalaryDayWheelPicker(
    day: Int,
    onDayChange: (Int) -> Unit,
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
            items = (1..31).toList().toImmutableList(),
            initialSelectedIndex = day - 1,
            onItemSelected = onDayChange,
            itemToString = { "${it}일" }
        )
    }
}