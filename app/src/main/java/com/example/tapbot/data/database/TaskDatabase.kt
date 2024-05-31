package com.example.tapbot.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tapbot.data.database.entities.ClickTask
import com.example.tapbot.data.database.entities.DelayTask
import com.example.tapbot.data.database.entities.StartLoop
import com.example.tapbot.data.database.entities.StopLoop
import com.example.tapbot.data.database.entities.TaskGroup

@Database(
    entities = [ClickTask::class, DelayTask::class, StartLoop::class, StopLoop::class, TaskGroup::class],
    version = 1)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

}