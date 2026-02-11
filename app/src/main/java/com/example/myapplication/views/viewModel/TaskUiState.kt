package com.example.myapplication.views.viewModel

import com.example.myapplication.model.TaskEntity

data class TaskUiState(
    val tasks: List<TaskEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val retryTask: TaskEntity? = null
)