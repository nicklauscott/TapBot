package com.example.tapbot.domain.usecases

import com.example.tapbot.domain.repository.TasksRepository
import javax.inject.Inject

class GetFavoriteActions @Inject constructor(private val repository: TasksRepository) {

    suspend operator fun invoke()  = repository.getFavoriteTasksGroup()
}