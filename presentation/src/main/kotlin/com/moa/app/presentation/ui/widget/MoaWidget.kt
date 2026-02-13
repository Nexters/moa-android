package com.moa.app.presentation.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.moa.app.core.model.widget.Widget
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.theme.Gray70
import com.moa.app.presentation.designsystem.theme.MoaTheme
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

class MoaWidget : GlanceAppWidget() {

    override val sizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val scope = rememberCoroutineScope()
            var uiState by rememberSaveable { mutableStateOf<MoaWidgetUiState>(MoaWidgetUiState.Loading) }
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                MoaWidgetEntryPoint::class.java
            )
            val repository = entryPoint.getWidgetRepository()

            suspend fun getWidgetUiState() {
                uiState = MoaWidgetUiState.Loading
                try {
                    val widget = repository.getWidget()
                    uiState = MoaWidgetUiState.Success(widget)
                } catch (e: Exception) {
                    uiState = MoaWidgetUiState.Error(e.message ?: "알 수 없는 에러")
                }
            }

            LaunchedEffect(Unit) {
                getWidgetUiState()
            }

            MoaTheme {
                MoaWidgetContent(
                    uiState = uiState,
                    onClickRefresh = {
                        scope.launch {
                            getWidgetUiState()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MoaWidgetContent(
    uiState: MoaWidgetUiState,
    onClickRefresh: () -> Unit,
) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .background(color = MoaTheme.colors.bgPrimary),
    ) {
        when (uiState) {
            is MoaWidgetUiState.Loading -> {
                MoaWidgetLoadingContent()
            }

            is MoaWidgetUiState.Success -> {
                when (uiState.widget) {
                    is Widget.Working -> MoaWidgetWorkingContent(
                        daySalary = uiState.widget.daySalary,
                        time = uiState.widget.time,
                        onClickRefresh = onClickRefresh,
                    )

                    is Widget.Vacation -> MoaWidgetVacationContent(
                        daySalary = uiState.widget.daySalary,
                        time = uiState.widget.time,
                        onClickRefresh = onClickRefresh,
                    )

                    is Widget.Finish -> MoaWidgetFinishContent(
                        monthlySalary = uiState.widget.monthlySalary,
                        onClickRefresh = onClickRefresh,
                    )
                }
            }

            is MoaWidgetUiState.Error -> {
                MoaWidgetErrorContent(onClickRefresh = onClickRefresh)
            }
        }
    }
}

@Composable
private fun MoaWidgetWorkingContent(
    daySalary: String,
    time: String,
    onClickRefresh: () -> Unit,
) {
    Image(
        modifier = GlanceModifier.fillMaxSize(),
        provider = ImageProvider(R.drawable.img_widget_working),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp,
            )
    ) {
        MoaWidgetChip(text = "근무중")

        Spacer(GlanceModifier.height(6.dp))

        MoaWidgetPrice(
            price = daySalary,
            priceColor = MoaTheme.colors.textGreen,
        )

        Spacer(GlanceModifier.defaultWeight())

        MoaWidgetTimeWithRefresh(
            time = time,
            onClickRefresh = onClickRefresh,
        )
    }
}

@Composable
private fun MoaWidgetVacationContent(
    daySalary: String,
    time: String,
    onClickRefresh: () -> Unit,
) {
    Image(
        modifier = GlanceModifier.fillMaxSize(),
        provider = ImageProvider(R.drawable.img_widget_vacation),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp,
            )
    ) {
        MoaWidgetChip(text = "휴가중")

        Spacer(GlanceModifier.height(6.dp))

        MoaWidgetPrice(
            price = daySalary,
            priceColor = MoaTheme.colors.textBlue,
        )

        Spacer(GlanceModifier.defaultWeight())

        MoaWidgetTimeWithRefresh(
            time = time,
            onClickRefresh = onClickRefresh,
        )
    }
}

@Composable
private fun MoaWidgetFinishContent(
    monthlySalary: String,
    onClickRefresh: () -> Unit,
) {
    Image(
        modifier = GlanceModifier.fillMaxSize(),
        provider = ImageProvider(R.drawable.img_widget_finish),
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable { onClickRefresh() }
            .padding(
                vertical = 20.dp,
                horizontal = 16.dp,
            )
    ) {
        Text(
            text = "이번달 누적 월급",
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = ColorProvider(
                    day = MoaTheme.colors.textMediumEmphasis,
                    night = MoaTheme.colors.textMediumEmphasis,
                ),
            ),
        )

        Spacer(GlanceModifier.height(4.dp))

        MoaWidgetPrice(
            price = monthlySalary,
            priceColor = MoaTheme.colors.textGreen,
        )
    }
}

@Composable
private fun MoaWidgetLoadingContent() {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(
                vertical = 24.dp,
                horizontal = 20.dp,
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            modifier = GlanceModifier.fillMaxWidth(),
            provider = ImageProvider(R.drawable.img_widget_loading),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun MoaWidgetErrorContent(onClickRefresh: () -> Unit) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(
                vertical = 24.dp,
                horizontal = 20.dp,
            ),
    ) {
        Spacer(GlanceModifier.height(36.dp))

        Text(
            modifier = GlanceModifier.fillMaxWidth(),
            text = "네트워크 연결 상태가\n" +
                    "좋지 않습니다",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = ColorProvider(
                    day = MoaTheme.colors.textMediumEmphasis,
                    night = MoaTheme.colors.textMediumEmphasis,
                ),
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(GlanceModifier.defaultWeight())

        Button(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(40.dp),
            text = "새로고침",
            onClick = onClickRefresh,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorProvider(
                    day = MoaTheme.colors.btnPrimaryEnable,
                    night = MoaTheme.colors.btnPrimaryEnable,
                ),
                contentColor = ColorProvider(
                    day = MoaTheme.colors.textHighEmphasisReverse,
                    night = MoaTheme.colors.textHighEmphasisReverse,
                ),
            )
        )
    }
}

@Composable
private fun MoaWidgetChip(text: String) {
    Text(
        modifier = GlanceModifier
            .padding(
                vertical = 2.dp,
                horizontal = 6.dp
            )
            .background(color = Gray70)
            .cornerRadius(4.dp),
        text = text,
        style = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = ColorProvider(
                day = MoaTheme.colors.textMediumEmphasis,
                night = MoaTheme.colors.textMediumEmphasis,
            ),
        ),
    )
}

@Composable
private fun MoaWidgetPrice(
    price: String,
    priceColor: Color,
) {
    Row {
        Text(
            text = price,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(
                    day = priceColor,
                    night = priceColor,
                ),
            ),
        )

        Spacer(GlanceModifier.width(2.dp))

        Text(
            text = "원",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = ColorProvider(
                    day = MoaTheme.colors.textMediumEmphasis,
                    night = MoaTheme.colors.textMediumEmphasis,
                ),
            ),
        )
    }
}

@Composable
private fun MoaWidgetTimeWithRefresh(
    time: String,
    onClickRefresh: () -> Unit,
) {
    Row(
        modifier = GlanceModifier.clickable { onClickRefresh() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = GlanceModifier.size(14.dp),
            provider = ImageProvider(R.drawable.ic_16_restart),
            contentDescription = null,
            colorFilter = ColorFilter.tint(
                colorProvider = ColorProvider(
                    day = MoaTheme.colors.textMediumEmphasis,
                    night = MoaTheme.colors.textMediumEmphasis,
                ),
            )
        )

        Spacer(GlanceModifier.width(2.dp))

        Text(
            text = "$time 기준",
            style = TextStyle(
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                color = ColorProvider(
                    day = MoaTheme.colors.textMediumEmphasis,
                    night = MoaTheme.colors.textMediumEmphasis,
                ),
            ),
        )
    }
}