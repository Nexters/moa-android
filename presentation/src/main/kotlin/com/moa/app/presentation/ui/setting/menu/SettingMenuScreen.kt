package com.moa.app.presentation.ui.setting.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun SettingMenuScreen() {

}

@Composable
private fun SettingMenuScreen(
    test: String
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.icon_back),
                            contentDescription = "Back",
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                }
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = MoaTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(MoaTheme.spacing.spacing20))

            SettingMenuUserInfoContent()

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            SettingMenuAppSettingContent()

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            SettingMenuAppInfoContent()

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            SettingMenuButtonContent()
        }
    }
}

@Composable
private fun SettingMenuUserInfoContent() {
    Text(
        text = "카카오 계정 회원",
        style = MoaTheme.typography.b2_400,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing4))

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "집계사장",
            style = MoaTheme.typography.t1_700,
            color = MoaTheme.colors.textGreen,
        )

        Spacer(Modifier.width(MoaTheme.spacing.spacing4))

        Image(
            painter = painterResource(R.drawable.icon_edit),
            contentDescription = "Edit",
        )
    }

    Spacer(Modifier.height(MoaTheme.spacing.spacing32))

    Text(
        text = "내정보",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable {},
        leadingContent = {
            Text(
                text = "월급 · 근무 정보 ",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun SettingMenuAppSettingContent() {
    Text(
        text = "앱 설정",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable {},
        leadingContent = {
            Text(
                text = "알림 설정",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun SettingMenuAppInfoContent() {
    Text(
        text = "앱 정보 및 도움말",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable {},
        leadingContent = {
            Text(
                text = "버전 정보",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable {},
        leadingContent = {
            Text(
                text = "약관 및 정책",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable {},
        leadingContent = {
            Text(
                text = "문의하기",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.icon_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun SettingMenuButtonContent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "로그아웃",
            style = MoaTheme.typography.b2_400,
            color = MoaTheme.colors.textMediumEmphasis,
        )

        VerticalDivider(
            modifier = Modifier
                .padding(horizontal = MoaTheme.spacing.spacing16)
                .height(12.dp),
            color = MoaTheme.colors.dividerSecondary,
        )

        Text(
            text = "회원탈퇴",
            style = MoaTheme.typography.b2_400,
            color = MoaTheme.colors.textMediumEmphasis,
        )
    }
}

@Preview
@Composable
private fun SettingMenuScreenPreview() {
    MoaTheme {
        SettingMenuScreen(test = "")
    }
}