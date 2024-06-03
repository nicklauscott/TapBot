package com.example.tapbot.domain.model

data class ServiceState(
    val running: Boolean = false,
    val runningTaskId: String? = null,
    val extraInfo: Any? = null
)

class TaskAlreadyRunningException(message: String): Exception(message)

class AccessibilityServiceNotEnabledException(message: String): Exception(message)