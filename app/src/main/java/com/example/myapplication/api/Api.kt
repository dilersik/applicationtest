package com.example.myapplication.api

import com.example.myapplication.model.MyList
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface Api {

    @GET("v1/d92aa39e-db04-499b-bfbb-4651c1291ccc")
    suspend fun getList(): List<MyList>

}