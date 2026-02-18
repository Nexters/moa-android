package com.moa.app.presentation.ui.history.scheduleform

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moa.app.core.model.history.LocalDateModel
import com.moa.app.core.model.history.Schedule
import com.moa.app.core.model.onboarding.Time
import com.moa.app.presentation.R
import com.moa.app.presentation.designsystem.component.DatePickerBottomSheet
import com.moa.app.presentation.designsystem.component.MoaTimeBottomSheet
import com.moa.app.presentation.designsystem.component.MoaTopAppBar
import com.moa.app.presentation.designsystem.component.ScheduleFormContent
import com.moa.app.presentation.designsystem.theme.MoaTheme

@Composable
fun ScheduleFormScreen(
    initialDate: LocalDateModel,
    schedule: Schedule?,
    viewModel: ScheduleFormViewModel = hiltViewModel(
        creationCallback = { factory: ScheduleFormViewModel.Factory ->
            factory.create(initialDate, schedule)
        }
    ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScheduleFormScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
    )

    if (uiState.showDateBottomSheet) {
        DatePickerBottomSheet(
            selectedDate = uiState.date,
            onConfirm = { viewModel.onIntent(ScheduleFormIntent.SetDate(it)) },
            onDismissRequest = { viewModel.onIntent(ScheduleFormIntent.ShowDateBottomSheet(false)) },
        )
    }

    if (uiState.showTimeBottomSheet) {
        MoaTimeBottomSheet(
            time = uiState.time,
            title = stringResource(R.string.schedule_form_work_time_title),
            onPositive = { viewModel.onIntent(ScheduleFormIntent.SetTime(it)) },
            onDismissRequest = { viewModel.onIntent(ScheduleFormIntent.ShowTimeBottomSheet(false)) },
        )
    }
}

@Composable
private fun ScheduleFormScreen(
    uiState: ScheduleFormUiState,
    onIntent: (ScheduleFormIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            MoaTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onIntent(ScheduleFormIntent.ClickBack) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_24_arrow_left),
                            contentDescription = stringResource(R.string.history_back_description),
                            tint = MoaTheme.colors.textHighEmphasis,
                        )
                    }
                }
            )
        },
        containerColor = MoaTheme.colors.bgPrimary,
    ) { innerPadding ->
        ScheduleFormContent(
            modifier = Modifier.padding(innerPadding),
            title = stringResource(if (uiState.isEditMode) R.string.schedule_form_title_edit else R.string.schedule_form_title_add),
            date = uiState.date,
            scheduleType = uiState.scheduleType,
            time = uiState.time,
            onDateClick = { onIntent(ScheduleFormIntent.ShowDateBottomSheet(true)) },
            onScheduleTypeSelect = { onIntent(ScheduleFormIntent.SelectScheduleType(it)) },
            onTimeClick = { onIntent(ScheduleFormIntent.ShowTimeBottomSheet(true)) },
            onCancelClick = { onIntent(ScheduleFormIntent.ClickCancel) },
            onConfirmClick = { onIntent(ScheduleFormIntent.ClickConfirm) },
        )
    }
}

@Preview
@Composable
private fun ScheduleFormScreenAddPreview() {
    MoaTheme {
        ScheduleFormScreen(
            uiState = ScheduleFormUiState(
                isEditMode = false,
                date = LocalDateModel(2026, 1, 20),
                scheduleType = ScheduleInputType.WORK,
                time = Time(9, 0, 18, 0),
            ),
            onIntent = {},
        )
    }
}

@Preview
@Composable
private fun ScheduleFormScreenEditPreview() {
    MoaTheme {
        ScheduleFormScreen(
            uiState = ScheduleFormUiState(
                isEditMode = true,
                date = LocalDateModel(2026, 1, 20),
                scheduleType = ScheduleInputType.VACATION,
                time = Time(9, 0, 18, 0),
            ),
            onIntent = {},
        )
    }
}