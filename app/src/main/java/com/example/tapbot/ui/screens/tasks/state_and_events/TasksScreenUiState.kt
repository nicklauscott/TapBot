package com.example.tapbot.ui.screens.tasks.state_and_events

import com.example.tapbot.domain.model.TaskGroup

data class TasksScreenUiState(
    val loading: Boolean = false,
    val tasks: List<TaskGroup> = emptyList()
)