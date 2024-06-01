package com.example.tapbot.ui.screens.tasks.taskslists.state_and_events

sealed class TasksScreenUiEvent {
    object GetFavoriteTasks: TasksScreenUiEvent()
    data class SearchTasks(val query: String): TasksScreenUiEvent()
}