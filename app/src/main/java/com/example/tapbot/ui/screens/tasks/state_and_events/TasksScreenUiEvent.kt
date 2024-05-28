package com.example.tapbot.ui.screens.tasks.state_and_events

sealed interface TasksScreenUiEvent {
    object CreateTask : TasksScreenUiEvent
}
