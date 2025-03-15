package com.example.myapplication.repository

import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper

interface PostRepository {

    suspend fun getList(isMock: Boolean = false): ResultWrapper<List<Post>>
}