package com.example.myapplication.repository

import com.example.myapplication.api.Api
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(private val api: Api) : PostRepository {

    private val mList = mutableListOf<Post>()

    override suspend fun getList(isMock: Boolean): ResultWrapper<List<Post>> {
        return try {
            val list = if (isMock) {
                listOf(
                    Post.Mock(1, "Title 1", "Body 1"),
                    Post.Mock(2, "Title 2", "Body 2"),
                    Post.Mock(3, "Title 3", "Body 3"),
                    Post.Mock(4, "Title 4", "Body 4"),
                    Post.Mock(5, "Title 5", "Body 5"),
                )

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
}