package com.example.myapplication.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.model.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}