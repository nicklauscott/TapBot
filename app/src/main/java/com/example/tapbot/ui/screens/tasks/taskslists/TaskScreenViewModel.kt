package com.example.tapbot.ui.screens.tasks.taskslists

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapbot.domain.usecases.GetAllTaskGroup
import com.example.tapbot.domain.usecases.GetFavoriteActions
import com.example.tapbot.domain.usecases.SearchTaskGroup
import com.example.tapbot.ui.screens.tasks.taskslists.state_and_events.TasksScreenUiEvent
import com.example.tapbot.ui.screens.tasks.taskslists.state_and_events.TasksScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskScreenViewModel @Inject constructor(
    private val getAllTaskGroup: GetAllTaskGroup,
    private val searchTaskGroup: SearchTaskGroup,
    private val getFavoriteActions: GetFavoriteActions
): ViewModel() {

    private val _state: MutableState<TasksScreenUiState> = mutableStateOf(TasksScreenUiState())
    val state: State<TasksScreenUiState> = _state

    init {
        _state.value = state.value.copy(loading = true)
        viewModelScope.launch {
            getAllTaskGroup.invoke().collect {
                withContext(Dispatchers.Main) {
                    _state.value = state.value.copy(loading = false, tasks = it, showingFavorite = false)
                }
            }
        }
    }

    fun onEvent(event: TasksScreenUiEvent) {
        when (event) {
            TasksScreenUiEvent.GetFavoriteTasks -> {
                if (!state.value.showingFavorite) {
                    _state.value = state.value.copy(loading = false, showingFavorite = true,
                        tasks = state.value.tasks.filter { it.favorite })
                }else {
                    _state.value = state.value.copy(loading = true)
                    viewModelScope.launch {
                        getAllTaskGroup.invoke().collect {
                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(loading = false, tasks = it, showingFavorite = false)
                            }
                        }
                    }
                }
            }
            is TasksScreenUiEvent.SearchTasks -> {
                viewModelScope.launch(Dispatchers.IO) {
                    searchTaskGroup(event.query).collect { taskGroups ->
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(tasks = taskGroups)
                        }
                    }
                }
            }
            TasksScreenUiEvent.ClearSearch -> {
                viewModelScope.launch {
                    getAllTaskGroup.invoke().collect {
                        withContext(Dispatchers.Main) {
                            _state.value = state.value.copy(loading = false, tasks = it, showingFavorite = false)
                        }
                    }
                }
            }
        }
    }
}