package com.moa.app.presentation.ui.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SampleScreen(viewModel : SampleViewModel = hiltViewModel()){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SampleScreen(
        uiState= uiState,
        onIntent = viewModel::onIntent,
    )
}

@Composable
private fun SampleScreen(
    uiState : SampleUiState,
    onIntent : (SampleIntent) -> Unit,
){
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = uiState.test)

            Button(onClick = {onIntent(SampleIntent.Click)}) {
                Text(text = "test")
            }
        }
    }
}

sealed interface SampleIntent {
    data object Click : SampleIntent
}