package com.moa.app.presentation.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalMoaColors = staticCompositionLocalOf<MoaColors> {
    error("No MoaColors provided")
}

val LocalMoaTypography = staticCompositionLocalOf<MoaTypography> {
    error("No MoaTypography provided")
}

val LocalMoaRadius = staticCompositionLocalOf<MoaRadius> {
    error("No MoaRadius provided")
}

val LocalMoaSpacing = staticCompositionLocalOf<MoaSpacing> {
    error("No MoaSpacing provided")
}

object MoaTheme {
    val colors: MoaColors
        @Composable
        get() = LocalMoaColors.current

    val typography: MoaTypography
        @Composable
        get() = LocalMoaTypography.current

    val radius: MoaRadius
        @Composable
        get() = LocalMoaRadius.current

    val spacing: MoaSpacing
        @Composable
        get() = LocalMoaSpacing.current
}

@Composable
fun MoaTheme(
    content: @Composable () -> Unit
) {
    val colors = MoaColors(
        bgPrimary = Gray90,
        bgSecondary = Gray80,
        containerPrimary = Gray80,
        containerSecondary = Gray70,
        textHighEmphasis = Gray0,
        textMediumEmphasis = Color(0x99FFFFFF),
        textLowEmphasis = Color(0x66FFFFFF),
        textDisabled = Color(0x47FFFFFF),
        textHighEmphasisReverse = Gray90,
        textGreen = Green40Main,
        textBlue = Blue,
        textError = Color(0xFFFF4037),
        textErrorLight = Color(0xFFFFDBDA),
        dividerPrimary = Gray80,
        dividerSecondary = Gray70,
        dimPrimary = Color(0x99000000),
        dimSecondary = Color(0x66000000),
        btnEnable = Green40Main,
        btnPressed = Green50,
        btnDisabled = Gray70,
    )

    val typography = MoaTypography(
        h1_700 = H1_700,
        h2_700 = H2_700,
        h2_500 = H2_500,
        h3_700 = H3_700,
        h3_500 = H3_500,
        t1_700 = T1_700,
        t1_500 = T1_500,
        t1_400 = T1_400,
        t2_700 = T2_700,
        t2_500 = T2_500,
        t2_400 = T2_400,
        t3_700 = T3_700,
        t3_500 = T3_500,
        t3_400 = T3_400,
        b1_600 = B1_600,
        b1_500 = B1_500,
        b1_400 = B1_400,
        b2_600 = B2_600,
        b2_500 = B2_500,
        b2_400 = B2_400,
        c1_600 = C1_600,
        c1_500 = C1_500,
        c1_400 = C1_400,
    )

    val radius = MoaRadius(
        radius4 = Radius4,
        radius8 = Radius8,
        radius12 = Radius12,
        radius16 = Radius16,
        radius20 = Radius20,
        radius999 = Radius999,
    )

    val spacing = MoaSpacing(
        spacing4 = spacing4,
        spacing8 = spacing8,
        spacing12 = spacing12,
        spacing16 = spacing16,
        spacing20 = spacing20,
        spacing24 = spacing24,
        spacing28 = spacing28,
        spacing32 = spacing32,
        spacing36 = spacing36,
        spacing40 = spacing40,
    )

    CompositionLocalProvider(
        LocalMoaColors provides colors,
        LocalMoaTypography provides typography,
        LocalMoaRadius provides radius,
        LocalMoaSpacing provides spacing,
    ) {
        content()
    }
}