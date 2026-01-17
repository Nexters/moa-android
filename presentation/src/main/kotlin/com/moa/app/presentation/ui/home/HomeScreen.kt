package com.moa.app.presentation.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel= hiltViewModel()) {
    Column {
        Text(text = "Home Screen")
        Button(onClick = viewModel::emit) {
            Text(text = "Go to History")
        }
    }
}