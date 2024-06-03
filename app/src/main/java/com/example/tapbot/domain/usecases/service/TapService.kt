package com.example.tapbot.domain.usecases.service

import android.content.Context
import com.example.tapbot.data.repository.ServiceRepositoryImpl
import com.example.tapbot.domain.model.Action
import javax.inject.Inject

class TapService @Inject constructor(private val serviceRepositoryImpl: ServiceRepositoryImpl) {
    operator fun invoke() = serviceRepositoryImpl.getTaskStatus()

    fun startTask(taskGroupId: String, actions: List<Action>) {
        serviceRepositoryImpl.startTask(taskGroupId, actions)
    }

    fun stopTask() =serviceRepositoryImpl.stopTask()
}
