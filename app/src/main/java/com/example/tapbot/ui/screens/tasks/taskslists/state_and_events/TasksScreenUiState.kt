package com.example.tapbot.ui.screens.tasks.taskslists.state_and_events

import com.example.tapbot.domain.model.TaskGroup

data class TasksScreenUiState(
    val loading: Boolean = false,
    val tasks: List<TaskGroup> = emptyList(),
    val showingFavorite: Boolean = false
)