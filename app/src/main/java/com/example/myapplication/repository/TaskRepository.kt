package com.example.myapplication.repository

import com.example.myapplication.model.ResultWrapper
import com.example.myapplication.model.TaskEntity
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAll(): Flow<List<TaskEntity>>

    suspend fun add(task: TaskEntity): ResultWrapper<Unit>

    suspend fun update(task: TaskEntity)

    suspend fun delete(task: TaskEntity)
}
