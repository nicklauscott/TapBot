package com.example.tapbot.data.repository

import com.example.tapbot.data.sevices.ServiceManager
import com.example.tapbot.domain.model.Action
import com.example.tapbot.domain.model.ServiceState
import com.example.tapbot.domain.repository.ServiceRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceManager: ServiceManager
): ServiceRepository {
    override fun startTask(taskGroupId: String, actions: List<Action>) {
        serviceManager.runTask(taskGroupId, actions)
    }

    override fun stopTask() {
        serviceManager.stopTask()
    }

    override fun getTaskStatus(): StateFlow<ServiceState> {
        return serviceManager.state
    }
}