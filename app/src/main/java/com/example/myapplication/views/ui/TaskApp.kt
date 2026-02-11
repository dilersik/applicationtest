package com.example.myapplication.views.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.views.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->

            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Retry",
                withDismissAction = true
            )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.retryLastTask()
            }

            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("To-Do List") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        TaskScreen(
            uiState = uiState,
            onAddTask = viewModel::addTask,
            onToggleTask = viewModel::toggleTaskCompletion,
            onDeleteTask = viewModel::deleteTask,
            paddingValues = padding
        )
    }
}
