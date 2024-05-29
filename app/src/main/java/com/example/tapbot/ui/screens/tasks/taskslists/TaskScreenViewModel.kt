package com.example.tapbot.ui.screens.tasks.taskslists

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.tapbot.ui.screens.tasks.taskslists.state_and_events.TasksScreenUiEvent
import com.example.tapbot.ui.screens.tasks.taskslists.state_and_events.TasksScreenUiState

class TaskScreenViewModel: ViewModel() {

    private val _state: MutableState<TasksScreenUiState> = mutableStateOf(TasksScreenUiState())
    val state: State<TasksScreenUiState> = _state

    init {
//        _state.value = state.value.copy(loading = true)
//        _state.value = state.value.copy(tasks = listOf(
//            TaskGroup(name = "Work", description = "Upgrading the app to the latest version of tap bot" ),
//            TaskGroup(name = "Work", description = "Upgrading the app to the latest version of tap bot" ),
//            TaskGroup(name = "Play", description = "Play a game about tap bot" ),
//            TaskGroup(name = "Play", description = "Play a game about tap bot" ),
//            TaskGroup(name = "Sing", description = "Sing a song about tap bot" ),
//            TaskGroup(name = "Sing", description = "Sing a song about tap bot" ),
//        ), loading = false)
    }

    fun onEvent(event: TasksScreenUiEvent) {
        when(event) {
            TasksScreenUiEvent.CreateTask -> TODO()
        }
    }
}