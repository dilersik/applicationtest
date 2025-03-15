package com.example.myapplication.model


sealed class Post {
    data class Remote(
        val id: Int,
        val title: String,
        val body: String,
        val address: String
    ) : Post()

    data class Mock(
        val id: Int,
        val title: String,
        val body: String,
    ) : Post()
}
