package com.example.myapplication.views

import com.example.myapplication.model.Post

sealed class Screen {
    data object Login : Screen()
    data object Posts : Screen()
    data object Validation : Screen()
    data object DateTimePicker : Screen()
    data class ScreenSaver(val posts: List<Post>) : Screen()
}

enum class ValidationState {
    LOADING, SUCCESS, FAILURE
}