package com.example.myapplication.repository

import com.example.myapplication.api.Api
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import javax.inject.Inject

class MyListRepositoryImpl @Inject constructor(private val api: Api): MyListRepository {
    override suspend fun getList(): ResultWrapper<List<Post>> {
        return try {
            val list = api.getList()
            ResultWrapper.Success(list)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}