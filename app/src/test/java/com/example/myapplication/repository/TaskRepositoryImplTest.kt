package com.example.myapplication.repository

import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.repository.database.TaskDao
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImplTest {

    private lateinit var taskDao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    private val dispatcher = UnconfinedTestDispatcher()

    private val sampleTask = TaskEntity(
        id = "1",
        title = "Test",
        description = "Description",
        isCompleted = false
    )

    @Before
    fun setup() {
        taskDao = mockk(relaxed = true)

        repository = TaskRepositoryImpl(
            taskDao = taskDao,
            dispatcher = dispatcher
        )
    }

    // --------------------------------------------------------
    // getAll()
    // --------------------------------------------------------

    @Test
    fun `getAll returns flow from dao`() = runTest {
        val flow = kotlinx.coroutines.flow.flowOf(listOf(sampleTask))
        every { taskDao.getAll() } returns flow

        val result = repository.getAll()

        assertEquals(listOf(sampleTask), result.first())
        verify { taskDao.getAll() }
    }

    // --------------------------------------------------------
    // add() success
    // --------------------------------------------------------

    @Test
    fun `add calls dao insert on success`() = runTest {
        coEvery { taskDao.insert(any()) } just Runs

        repository = TaskRepositoryImpl(
            taskDao = taskDao,
            dispatcher = UnconfinedTestDispatcher(),
            forceNetworkSuccess = true
        )

        val result = repository.add(sampleTask)

        assertTrue(result is ResultWrapper.Success)
        coVerify { taskDao.insert(sampleTask) }
    }

    // --------------------------------------------------------
    // add() failure (force by flag)
    // --------------------------------------------------------

    @Test
    fun `add calls dao insert on failure`() = runTest {
        coEvery { taskDao.insert(any()) } just Runs

        repository = TaskRepositoryImpl(
            taskDao = taskDao,
            dispatcher = UnconfinedTestDispatcher(),
            forceNetworkSuccess = false
        )

        val result = repository.add(sampleTask)

        assertTrue(result is ResultWrapper.Error)
        coVerify(exactly = 0) { taskDao.insert(sampleTask) }
    }

    // --------------------------------------------------------
    // add() failure (force by throwing)
    // --------------------------------------------------------

    @Test
    fun `add returns error if dao throws`() = runTest {
        coEvery { taskDao.insert(any()) } throws RuntimeException("DB error")

        val result = repository.add(sampleTask)

        assertTrue(result is ResultWrapper.Error)
    }

    // --------------------------------------------------------
    // update()
    // --------------------------------------------------------

    @Test
    fun `update calls dao update`() = runTest {
        coEvery { taskDao.update(any()) } just Runs

        repository.update(sampleTask)

        coVerify { taskDao.update(sampleTask) }
    }

    // --------------------------------------------------------
    // delete()
    // --------------------------------------------------------

    @Test
    fun `delete calls dao delete`() = runTest {
        coEvery { taskDao.delete(any()) } just Runs

        repository.delete(sampleTask)

        coVerify { taskDao.delete(sampleTask) }
    }
}