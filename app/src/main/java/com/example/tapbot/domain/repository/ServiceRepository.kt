package com.example.tapbot.domain.repository

import com.example.tapbot.domain.model.Action
import com.example.tapbot.domain.model.ServiceState
import kotlinx.coroutines.flow.StateFlow

interface ServiceRepository {
    fun startTask(taskGroupId: String, actions: List<Action>)
    fun stopTask()
    fun getTaskStatus(): StateFlow<ServiceState>
}