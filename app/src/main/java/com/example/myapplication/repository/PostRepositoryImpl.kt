package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.api.Api
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: Api,
    private val context: Context
) : PostRepository {

    private val mList = mutableListOf<Post>()

    override suspend fun getList(isMock: Boolean): ResultWrapper<List<Post>> {
        return try {
            val list = if (isMock) {
                readMockPostsFromJson()
            } else if (mList.isEmpty()) {
                mList.addAll(api.getList())
                mList
            } else {
                mList.take(256)
            }
            ResultWrapper.Success(list)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    private suspend fun readMockPostsFromJson(): List<Post> = withContext(Dispatchers.IO) {
        val inputStream = context.assets.open("mock_posts.json")
        val reader = InputStreamReader(inputStream)
        val type = object : TypeToken<List<Post>>() {}.type
        Gson().fromJson<List<Post>>(reader, type)
    }
}