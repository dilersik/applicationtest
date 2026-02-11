package com.example.myapplication.repository

import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.model.TaskEntity
import com.example.myapplication.repository.database.TaskDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val forceNetworkSuccess: Boolean? = null
) : TaskRepository {

    override fun getAll(): Flow<List<TaskEntity>> {
        return taskDao.getAll()
    }

    override suspend fun add(task: TaskEntity): ResultWrapper<Unit> =
        withContext(dispatcher) {
            try {
                simulateNetworkCall()

                taskDao.insert(task)

                ResultWrapper.Success(Unit)
            } catch (e: Exception) {
                ResultWrapper.Error(e)
            }
        }

    override suspend fun update(task: TaskEntity) =
        withContext(dispatcher) {
            taskDao.update(task)
        }

    override suspend fun delete(task: TaskEntity) =
        withContext(dispatcher) {
            taskDao.delete(task)
        }

    private suspend fun simulateNetworkCall() {
        val success = forceNetworkSuccess ?: run {
            val delayTime = (500L..2500L).random()
            delay(delayTime)
            (1..100).random() <= 75
        }

        if (!success) {
            throw Exception("Network request failed")
        }
    }
}