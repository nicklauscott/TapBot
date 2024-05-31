package com.example.tapbot.domain.repository

import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup
import com.example.tapbot.domain.model.TaskGroupList
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    suspend fun getAllTasksGroup(): Flow<List<TaskGroup>>
    suspend fun searchTasksGroup(query: String): Flow<List<TaskGroup>>
    suspend fun getFavoriteTasksGroup(): Flow<List<TaskGroup>>
    suspend fun getTasksGroupById(tasksGroupId: String): Flow<TaskGroupList>
    suspend fun saveTasksGroup(tasksGroup: TaskGroup, tasks: List<Task>)
    suspend fun deleteTasksGroup(tasksGroupId: String)
}