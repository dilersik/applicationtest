package com.example.myapplication.api

import com.example.myapplication.model.Post
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface Api {

    @GET("posts")
    suspend fun getList(): List<Post.Remote>

}