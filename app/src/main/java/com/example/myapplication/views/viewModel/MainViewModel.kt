package com.example.myapplication.views.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            repository.getAll()
                .collect { tasks ->
                    _uiState.value = _uiState.value.copy(
                        tasks = tasks,
                        errorMessage = null
                    )
                }
        }
    }

    fun addTask(title: String, description: String) {
        // Validation
        if (title.length > 50) {
            emitError("Title cannot exceed 50 characters")
            return
        }

        if (description.length > 200) {
            emitError("Description cannot exceed 200 characters")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            val newTask = TaskEntity(
                title = title,
                description = description
            )

            when (repository.add(newTask)) {
                is ResultWrapper.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        retryTask = null
                    )
                }

                is ResultWrapper.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to add task",
                        retryTask = newTask
                    )
                }
            }
        }
    }

    fun retryLastTask() {
        _uiState.value.retryTask?.let {
            addTask(it.title, it.description)
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.update(
                task.copy(isCompleted = !task.isCompleted)
            )
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    private fun emitError(message: String) {
        _uiState.value = _uiState.value.copy(
            errorMessage = message
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }
}
