package com.example.tapbot.domain.usecases

import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup
import com.example.tapbot.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTaskGroup @Inject constructor(private val repository: TasksRepository) {

    suspend operator fun invoke(query: String): Flow<List<TaskGroup>> {
        return repository.searchTasksGroup(query)
    }
}