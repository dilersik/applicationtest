package com.example.myapplication.views.viewModel

import android.content.SharedPreferences
import app.cash.turbine.test
import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.model.Post
import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.repository.PostRepository
import com.example.myapplication.views.Screen
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: MainViewModel
    private lateinit var repository: PostRepository
    private lateinit var sharedPrefs: SharedPreferences

    @Before
    fun setUp() {
        repository = mockk()
        sharedPrefs = mockk(relaxed = true)
    }

    @Test
    fun `getList should update list on success`() = runTest {
        // Given
        val fakePosts = listOf(
            Post("Shop1", "Desc", null, 4.2, "Addr", listOf(1.0, 2.0), "maps", "site")
        )
        coEvery { repository.getList(true) } returns ResultWrapper.Success(fakePosts)

        // When
        viewModel = MainViewModel(repository, sharedPrefs)
        viewModel.getList()

        // Then
        viewModel.list.test {
            val result = awaitItem()
            assertEquals(fakePosts, result)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `goToScreen should update currentScreen`() = runTest {
        val post = Post("Shop1", "Desc", null, 4.2, "Addr", listOf(1.0, 2.0), "maps", "site")
        val screenDetails = Screen.Details(post)
        viewModel = MainViewModel(repository, sharedPrefs)

        viewModel.goToScreen(screenDetails)

        assertEquals(screenDetails, viewModel.currentScreen.value)
    }
}