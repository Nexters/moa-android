package com.moa.app.presentation.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moa.app.presentation.R

@Immutable
data class MoaTypography(
    val h1_700: TextStyle,
    val h2_700: TextStyle,
    val h2_500: TextStyle,
    val h3_700: TextStyle,
    val h3_500: TextStyle,
    val t1_700: TextStyle,
    val t1_500: TextStyle,
    val t1_400: TextStyle,
    val t2_700: TextStyle,
    val t2_500: TextStyle,
    val t2_400: TextStyle,
    val t3_700: TextStyle,
    val t3_500: TextStyle,
    val t3_400: TextStyle,
    val b1_600: TextStyle,
    val b1_500: TextStyle,
    val b1_400: TextStyle,
    val b2_600: TextStyle,
    val b2_500: TextStyle,
    val b2_400: TextStyle,
    val c1_600: TextStyle,
    val c1_500: TextStyle,
    val c1_400: TextStyle,
)

private val Pretendard = FontFamily(
    Font(resId = R.font.pretendard_bold, weight = FontWeight.Bold),
    Font(resId = R.font.pretendard_medium, weight = FontWeight.Medium),
    Font(resId = R.font.pretendard_regular, weight = FontWeight.Normal),
    Font(resId = R.font.pretendard_semibold, weight = FontWeight.SemiBold),
)

// --- Headline ---
val H1_700 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 40.sp,
    lineHeight = 50.sp,
    letterSpacing = (-0.2).sp
)
val H2_700 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 36.sp,
    lineHeight = 48.sp,
    letterSpacing = (-0.2).sp
)
val H2_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 36.sp,
    lineHeight = 48.sp,
    letterSpacing = (-0.2).sp
)
val H3_700 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 38.sp,
    letterSpacing = (-0.2).sp
)
val H3_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 28.sp,
    lineHeight = 38.sp,
    letterSpacing = (-0.2).sp
)

// --- Title ---
val T1_700 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.2).sp
)
val T1_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 24.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.2).sp
)
val T1_400 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
    lineHeight = 34.sp,
    letterSpacing = (-0.2).sp
)
val T2_700 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = (-0.2).sp
)
val T2_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = (-0.2).sp
)
val T2_400 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 20.sp,
    lineHeight = 28.sp,
    letterSpacing = (-0.2).sp
)
val T3_700 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = (-0.2).sp
)
val T3_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = (-0.2).sp
)
val T3_400 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    lineHeight = 26.sp,
    letterSpacing = (-0.2).sp
)

// --- Body ---
val B1_600 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = (-0.2).sp
)
val B1_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = (-0.2).sp
)
val B1_400 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
    letterSpacing = (-0.2).sp
)
val B2_600 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.SemiBold,
    fontSize = 14.sp,
    lineHeight = 21.sp,
    letterSpacing = (-0.2).sp
)
val B2_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 21.sp,
    letterSpacing = (-0.2).sp
)
val B2_400 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    lineHeight = 21.sp,
    letterSpacing = (-0.2).sp
)

// --- Caption ---
val C1_600 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = (-0.2).sp
)
val C1_500 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = (-0.2).sp
)
val C1_400 = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 18.sp,
    letterSpacing = (-0.2).sp
)
