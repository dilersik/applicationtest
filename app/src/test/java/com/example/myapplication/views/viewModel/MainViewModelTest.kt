package com.example.myapplication.views.viewModel

import app.cash.turbine.test
import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val repository: TaskRepository = mockk(relaxed = true)

    private lateinit var viewModel: MainViewModel

    private val sampleTask = TaskEntity(
        id = "1",
        title = "Task",
        description = "Description",
        isCompleted = false
    )

    @Before
    fun setup() {
        every { repository.getAll() } returns flowOf(emptyList())
        this.viewModel = MainViewModel(repository)
    }

    // ----------------------------------------------------
    // Initial state
    // ----------------------------------------------------

    @Test
    fun `initial state is empty and not loading`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.tasks.isEmpty())
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }
    }

    // ----------------------------------------------------
    // Observe tasks
    // ----------------------------------------------------

    @Test
    fun `tasks are emitted from repository`() = runTest {
        every { repository.getAll() } returns flowOf(listOf(sampleTask))

        viewModel = MainViewModel(repository)

        viewModel.uiState.test {

            // Initial state
            val initial = awaitItem()
            assertTrue(initial.tasks.isEmpty())

            // Collected state
            val updated = awaitItem()
            assertEquals(1, updated.tasks.size)
            assertEquals(sampleTask, updated.tasks.first())
        }
    }

    // ----------------------------------------------------
    // Add task - success
    // ----------------------------------------------------

    @Test
    fun `addTask success updates loading state`() = runTest {
        coEvery { repository.add(any()) } returns ResultWrapper.Success(Unit)

        viewModel.uiState.test {

            // Initial state
            val initial = awaitItem()
            assertFalse(initial.isLoading)

            viewModel.addTask("Title", "Description")

            // Loading state
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            // Final state
            val success = awaitItem()
            assertFalse(success.isLoading)
        }

        coVerify { repository.add(any()) }
    }

    // ----------------------------------------------------
    // Add task - failure
    // ----------------------------------------------------

    @Test
    fun `addTask failure emits error and retryTask`() = runTest {
        coEvery { repository.add(any()) } returns ResultWrapper.Error(Exception())

        viewModel.uiState.test {

            // 1️⃣ Initial emission
            val initial = awaitItem()
            assertFalse(initial.isLoading)

            viewModel.addTask("Title", "Description")

            // 2️⃣ Loading state
            val loading = awaitItem()
            assertTrue(loading.isLoading)

            // 3️⃣ Error state
            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertNotNull(errorState.errorMessage)
            assertNotNull(errorState.retryTask)
        }
    }

    // ----------------------------------------------------
    // Retry last task
    // ----------------------------------------------------

    @Test
    fun `retryLastTask retries failed task`() = runTest {
        coEvery { repository.add(any()) } returnsMany listOf(
            ResultWrapper.Error(Exception()),
            ResultWrapper.Success(Unit)
        )

        viewModel.addTask("Retry", "Me")
        advanceUntilIdle()

        viewModel.retryLastTask()
        advanceUntilIdle()

        coVerify(exactly = 2) { repository.add(any()) }
    }

    // ----------------------------------------------------
    // Validation
    // ----------------------------------------------------

    @Test
    fun `title longer than 50 chars emits error`() = runTest {
        viewModel.addTask("a".repeat(51), "desc")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Title cannot exceed 50 characters", state.errorMessage)
        }
    }

    @Test
    fun `description longer than 200 chars emits error`() = runTest {
        viewModel.addTask("title", "a".repeat(201))

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Description cannot exceed 200 characters", state.errorMessage)
        }
    }

    // ----------------------------------------------------
    // Toggle completion
    // ----------------------------------------------------

    @Test
    fun `toggleTaskCompletion updates task`() = runTest {
        viewModel.toggleTaskCompletion(sampleTask)

        advanceUntilIdle()

        coVerify {
            repository.update(
                sampleTask.copy(isCompleted = true)
            )
        }
    }

    // ----------------------------------------------------
    // Delete task
    // ----------------------------------------------------

    @Test
    fun `deleteTask removes task`() = runTest {
        viewModel.deleteTask(sampleTask)

        advanceUntilIdle()

        coVerify { repository.delete(sampleTask) }
    }

    // ----------------------------------------------------
    // Clear error
    // ----------------------------------------------------

    @Test
    fun `clearError resets error message`() = runTest {
        viewModel.addTask("a".repeat(51), "desc")
        viewModel.clearError()

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.errorMessage)
        }
    }
}
