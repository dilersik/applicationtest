package com.example.myapplication.model

sealed class ResultWrapper<T> {
    class Error<T>(val exception: Exception) : ResultWrapper<T>()
    class Success<T>(val value: T) : ResultWrapper<T>()

}