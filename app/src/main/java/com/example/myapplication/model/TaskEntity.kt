package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntity(

    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val title: String,

    val description: String,

    val isCompleted: Boolean = false,

    val createdAt: Long = System.currentTimeMillis()
)
