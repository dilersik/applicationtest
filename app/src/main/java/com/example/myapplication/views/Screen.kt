package com.example.myapplication.views

sealed class Screen {
    data object Login : Screen()
    data object Posts : Screen()
    data object Validation : Screen()
    data object DateTimePicker : Screen()
    data object ScreenSaver : Screen()
}

enum class ValidationState {
    LOADING, SUCCESS, FAILURE
}