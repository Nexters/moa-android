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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.model.setting.OAuthType
import com.moa.app.core.model.setting.SettingMenu
import com.moa.app.presentation.BuildConfig
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.MoaRow
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun SettingMenuScreen(viewModel: SettingMenuViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingMenuScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun SettingMenuScreen(
    uiState: SettingUiState,
    onIntent: (SettingMenuIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                title = {
                    Text(
                        text = "설정",
                        style = MoaTheme.typography.t3_500,
                        color = MoaTheme.colors.textHighEmphasis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(SettingMenuIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
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

            SettingMenuUserInfoContent(
                oAuthType = uiState.settingMenu?.oAuthType,
                nickName = uiState.settingMenu?.nickName ?: "",
                onIntent = onIntent,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            SettingMenuAppSettingContent(onIntent = onIntent)

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            SettingMenuAppInfoContent(
                latestAppVersion = uiState.settingMenu?.latestAppVersion ?: "",
                onIntent = onIntent,
            )

            Spacer(Modifier.height(MoaTheme.spacing.spacing24))

            SettingMenuButtonContent(onIntent = onIntent)
        }
    }
}

@Composable
private fun SettingMenuUserInfoContent(
    oAuthType: OAuthType?,
    nickName: String,
    onIntent: (SettingMenuIntent) -> Unit
) {
    if (oAuthType != null) {
        Text(
            text = "${oAuthType.title} 계정 회원",
            style = MoaTheme.typography.b2_400,
            color = MoaTheme.colors.textMediumEmphasis,
        )

        Spacer(Modifier.height(MoaTheme.spacing.spacing4))
    }

    Row(
        modifier = Modifier.clickable { onIntent(SettingMenuIntent.ClickNickName) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = nickName,
            style = MoaTheme.typography.t1_700,
            color = MoaTheme.colors.textGreen,
        )

        Spacer(Modifier.width(MoaTheme.spacing.spacing4))

        Image(
            painter = painterResource(R.drawable.ic_20_edit),
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
        modifier = Modifier.clickable { onIntent(SettingMenuIntent.ClickWorkInfo) },
        leadingContent = {
            Text(
                text = "월급 · 근무 정보",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun SettingMenuAppSettingContent(onIntent: (SettingMenuIntent) -> Unit) {
    Text(
        text = "앱 설정",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable { onIntent(SettingMenuIntent.ClickNotificationSetting) },
        leadingContent = {
            Text(
                text = "알림 설정",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun SettingMenuAppInfoContent(
    latestAppVersion: String,
    onIntent: (SettingMenuIntent) -> Unit,
) {
    val currentAppVersion = BuildConfig.APP_VERSION_NAME
    val isLatest = currentAppVersion == latestAppVersion

    Text(
        text = "앱 정보 및 도움말",
        style = MoaTheme.typography.b2_500,
        color = MoaTheme.colors.textMediumEmphasis,
    )

    Spacer(Modifier.height(MoaTheme.spacing.spacing8))

    MoaRow(
        modifier = Modifier.clickable {
            // TODO 플레이스토어
        },
        leadingContent = {
            Text(
                text = "버전 정보",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = "v$currentAppVersion",
                style = MoaTheme.typography.b1_400,
                color = MoaTheme.colors.textMediumEmphasis,
            )
        },
        subTrailingContent = {
            if (!isLatest) {
                Text(
                    text = "업데이트 필요",
                    style = MoaTheme.typography.b1_500,
                    color = MoaTheme.colors.textGreen,
                )
            }
        },
        trailingContent = {
            if (isLatest) {
                Text(
                    text = "최신 버전",
                    style = MoaTheme.typography.b1_500,
                    color = MoaTheme.colors.textGreen,
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.ic_24_chevron_right),
                    contentDescription = "Chevron Right",
                )
            }
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable { onIntent(SettingMenuIntent.ClickTerms) },
        leadingContent = {
            Text(
                text = "약관 및 정책",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )

    Spacer(Modifier.height(10.dp))

    MoaRow(
        modifier = Modifier.clickable {
            // TODO 이메일 앱 띄우기
        },
        leadingContent = {
            Text(
                text = "문의하기",
                style = MoaTheme.typography.b1_500,
                color = MoaTheme.colors.textHighEmphasis,
            )
        },
        trailingContent = {
            Image(
                painter = painterResource(R.drawable.ic_24_chevron_right),
                contentDescription = "Chevron Right",
            )
        }
    )
}

@Composable
private fun SettingMenuButtonContent(onIntent: (SettingMenuIntent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.clickable { onIntent(SettingMenuIntent.ClickLogout) },
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
            modifier = Modifier.clickable { onIntent(SettingMenuIntent.ClickWithdraw) },
            text = "회원탈퇴",
            style = MoaTheme.typography.b2_400,
            color = MoaTheme.colors.textMediumEmphasis,
        )
    }
}

sealed interface SettingMenuIntent {
    data object ClickBack : SettingMenuIntent
    data object ClickNickName : SettingMenuIntent
    data object ClickWorkInfo : SettingMenuIntent
    data object ClickNotificationSetting : SettingMenuIntent
    data object ClickTerms : SettingMenuIntent
    data object ClickLogout : SettingMenuIntent
    data object ClickWithdraw : SettingMenuIntent
}

@Preview
@Composable
private fun SettingMenuScreenPreview() {
    MoaTheme {
        SettingMenuScreen(
            uiState = SettingUiState(
                settingMenu = SettingMenu(
                    oAuthType = OAuthType.KAKAO,
                    nickName = "집계사장",
                    latestAppVersion = "1.0.0",
                )
            ),
            onIntent = {},
        )
    }
}