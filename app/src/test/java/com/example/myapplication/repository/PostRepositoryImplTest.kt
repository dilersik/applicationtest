package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.api.Api
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostRepositoryImplTest {

    private lateinit var repository: PostRepositoryImpl
    private lateinit var api: Api
    private lateinit var context: Context

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        api = mockk()
        context = mockk()
        repository = PostRepositoryImpl(api, context)
    }

    @Test
    fun `getList returns mock data when isMock is true`() = runTest {
        // Given
        val mockJson = """
            [{"name":"Shop1","description":"Desc","picture":null,"rating":4.2,"address":"Addr","coordinates":[1.0,2.0],"google_maps_link":"maps","website":"site"}]
        """.trimIndent()
        val mockInput = mockJson.byteInputStream()

        every { context.assets.open("mock_posts.json") } returns mockInput

        // When
        val result = repository.getList(true)

        // Then
        assert(result is ResultWrapper.Success)
        assert((result as ResultWrapper.Success).value.first().name == "Shop1")
    }

    @Test
    fun `getList fetches from API if not mock and cache is empty`() = runTest {
        // Given
        val fakePosts = listOf(
            Post("Shop A", "Desc", null, 4.5, "Addr", listOf(1.0, 2.0), "maps", "site")
        )
        coEvery { api.getList() } returns fakePosts

        // When
        val result = repository.getList(false)

        // Then
        assert(result is ResultWrapper.Success)
        assert((result as ResultWrapper.Success).value == fakePosts)
    }

    @Test
    fun `getList returns error on exception`() = runTest {
        // Given
        coEvery { api.getList() } throws RuntimeException("Network failure")

        // When
        val result = repository.getList(false)

        // Then
        assert(result is ResultWrapper.Error)
    }
}