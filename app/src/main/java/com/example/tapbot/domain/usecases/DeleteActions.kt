package com.example.tapbot.domain.usecases

import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.repository.TasksRepository
import javax.inject.Inject

class DeleteActions @Inject constructor(private val repository: TasksRepository) {

    suspend operator fun invoke(tasGroupId: String)  {
        repository.deleteTasksGroup(tasGroupId)
    }

    suspend fun deleteTask(task: Task) {
        repository.deleteTask(task)
    }
}