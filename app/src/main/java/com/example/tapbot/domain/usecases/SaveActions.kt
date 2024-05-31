package com.example.tapbot.domain.usecases

import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup
import com.example.tapbot.domain.repository.TasksRepository
import javax.inject.Inject

class SaveActions @Inject constructor(private val repository: TasksRepository) {

    suspend operator fun invoke(tasGroup: TaskGroup, tasks: List<Task>)  {
        repository.saveTasksGroup(tasGroup, tasks)
    }
}