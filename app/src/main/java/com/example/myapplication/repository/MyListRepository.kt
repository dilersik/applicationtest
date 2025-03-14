package com.example.myapplication.repository

import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper

interface MyListRepository {

    suspend fun getList(): ResultWrapper<List<Post>>
}