package com.example.tapbot.ui.screens.tasks.taskslists.state_and_events

sealed interface TasksScreenUiEvent {
    object CreateTask : TasksScreenUiEvent
}
