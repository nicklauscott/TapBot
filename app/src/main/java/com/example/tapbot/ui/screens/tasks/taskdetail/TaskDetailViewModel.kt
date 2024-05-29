package com.example.tapbot.ui.screens.tasks.taskdetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapbot.domain.model.Actions
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.usecases.taskbuilder.TaskBuilder
import com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events.TaskDetailScreenState
import com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events.TaskDetailScreenUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    saveStateHandle: SavedStateHandle
): ViewModel() {

    private val taskBuilder = TaskBuilder().builder()

    private val _state: MutableState<TaskDetailScreenState> = mutableStateOf(TaskDetailScreenState())
    val state: State<TaskDetailScreenState> = _state

    private val _channel: Channel<TaskDetailUiChannel> = Channel(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    init {
        val id = saveStateHandle.get<String>("taskId")
        state.value.update(null, emptyList())
    }

    fun onEvent(event: TaskDetailScreenUiEvent) {
        when (event) {
            is TaskDetailScreenUiEvent.AddAction -> addTask(event.action)
            is TaskDetailScreenUiEvent.DeleteAction -> deleteTask(event.taskIndex)
            is TaskDetailScreenUiEvent.EditAction -> editTask(event.index, event.task)
            TaskDetailScreenUiEvent.SaveTask -> save()
            TaskDetailScreenUiEvent.PlayTask -> { }
        }
    }

    private fun save() {
        _state.value = state.value.copy(saving = true)
        viewModelScope.launch(Dispatchers.IO) {
            val taskList = taskBuilder.build()
            // save to database
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(saving = false)
                state.value.update(state.value.taskGroup, state.value.taskList)
            }
        }
    }

    private fun deleteTask(index: Int) {
        state.value.taskList.toMutableList().also {
            it.removeAt(index)
            state.value.update(state.value.taskGroup, it)
            _state.value = state.value.copy(taskList = it)
            taskBuilder.deleteTask(index) // --
        }
    }

    private fun editTask(index: Int, task: Task) {
        val newTaskList = state.value.taskList.toMutableList()
        newTaskList.removeAt(index)
        newTaskList.add(index, task)
        state.value.update(state.value.taskGroup, newTaskList)
        _state.value = state.value.copy(taskList = newTaskList)
        taskBuilder.editTask(index, task) // ---
    }

    private fun addTask(task: Actions) {
        try {
             when (task) {
                 Actions.CLICK -> taskBuilder.click()
                 Actions.DELAY -> taskBuilder.delay(delay = 1)
                 Actions.LOOP -> taskBuilder.loop()
                 Actions.STOP_LOOP -> taskBuilder.stopLoop()
             }
            state.value.update(state.value.taskGroup, taskBuilder.getTempTaskList())
            _state.value = state.value.copy(taskList = taskBuilder.getTempTaskList())
        }catch (ex: Exception) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.AddingTaskError(ex.message.toString()))
            }
        }
    }


    sealed class TaskDetailUiChannel {
        data class AddingTaskError(val message: String): TaskDetailUiChannel()
    }


}