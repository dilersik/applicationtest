package com.example.myapplication.views

import com.example.myapplication.model.Post

sealed class Screen() {
    object Posts : Screen()
    data class Details(val post: Post) : Screen()
}

enum class ValidationState {
    LOADING, SUCCESS, FAILURE
}