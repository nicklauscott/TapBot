package com.example.tapbot.ui.screens.tasks.taskdetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapbot.domain.model.Actions
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.usecases.DeleteActions
import com.example.tapbot.domain.usecases.GetTaskGroupAction
import com.example.tapbot.domain.usecases.SaveActions
import com.example.tapbot.domain.usecases.taskbuilder.TaskBuilder
import com.example.tapbot.domain.usecases.taskbuilder.TaskManager
import com.example.tapbot.domain.usecases.taskbuilder.TaskManagerError
import com.example.tapbot.domain.usecases.taskbuilder.TaskManagerWarning
import com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events.TaskDetailScreenState
import com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events.TaskDetailScreenUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val getTaskGroupAction: GetTaskGroupAction,
    private val saveActions: SaveActions,
    private val deleteActions: DeleteActions,
    saveStateHandle: SavedStateHandle
): ViewModel() {

    private val taskBuilder = TaskBuilder().builder()

    private val _state: MutableState<TaskDetailScreenState> = mutableStateOf(TaskDetailScreenState())
    val state: State<TaskDetailScreenState> = _state

    private val _channel: Channel<TaskDetailUiChannel> = Channel(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    init {
        val taskGroupId = saveStateHandle.get<String>("taskId")
        _state.value = state.value.copy(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            taskGroupId?.let { id ->
                getTaskGroupAction(id).collect {
                    withContext(Dispatchers.Main) {
                        _state.value = state.value.copy(loading = false, taskGroup = it.taskGroup, taskList = it.tasks)
                        state.value.update(taskGroup = it.taskGroup, taskList = it.tasks)
                    }
                }
            }
        }
    }

    fun onEvent(event: TaskDetailScreenUiEvent) {
        when (event) {
            is TaskDetailScreenUiEvent.AddAction -> addTask(event.action)
            is TaskDetailScreenUiEvent.DeleteAction -> deleteTask(event.taskIndex)
            is TaskDetailScreenUiEvent.EditAction -> editTask(event.index, event.task)
            TaskDetailScreenUiEvent.SaveTask -> save()
            TaskDetailScreenUiEvent.PlayTask -> play()
        }
    }

    private fun play() {
        viewModelScope.launch(Dispatchers.IO) {
            val taskList = taskBuilder.getActions()
            _channel.send(TaskDetailUiChannel.RunActions(taskList))
        }
    }

    private fun save() {
        _state.value = state.value.copy(saving = true)
        viewModelScope.launch(Dispatchers.IO) {

            // save to database
            state.value.taskGroup?.let { saveActions(it, taskBuilder.build()) }

            // update state
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(saving = false)
                state.value.update(state.value.taskGroup, state.value.taskList)
            }
        }
    }

    private fun deleteTask(index: Int) {
        try {
            if (taskBuilder.canDeleteTask(index)) {
                state.value.taskList.toMutableList().also {
                    it.removeAt(index)
                    state.value.update(state.value.taskGroup, it)
                    _state.value = state.value.copy(taskList = it)
                }
                taskBuilder.deleteTask(index)
            }
        }catch (warning: TaskManagerWarning) {
            state.value.taskList.toMutableList().also {
                it.removeAt(index)
                state.value.update(state.value.taskGroup, it)
                _state.value = state.value.copy(taskList = it)
            }
            taskBuilder.deleteTask(index)

            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerWarning(warning.message.toString()))
            }
        }catch (ex: TaskManagerError) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerError(ex.message.toString()))
            }
        }catch (ex: Exception) {
            Log.e("TaskDetailViewModel", ex.message.toString())
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
                 Actions.DELAY -> taskBuilder.delay()
                 Actions.LOOP -> taskBuilder.loop()
                 Actions.STOP_LOOP -> taskBuilder.stopLoop()
             }
            state.value.update(state.value.taskGroup, taskBuilder.getTempTaskList())
            _state.value = state.value.copy(taskList = taskBuilder.getTempTaskList())
        }catch (ex: TaskManagerError) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerError(ex.message.toString()))
            }
        }catch (warning: TaskManagerWarning) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerWarning(warning.message.toString()))
            }
        }catch (ex: Exception) {
            Log.e("TaskDetailViewModel", ex.message.toString())
        }
    }


    sealed class TaskDetailUiChannel {
        data class TaskMangerError(val message: String): TaskDetailUiChannel()
        data class TaskMangerWarning(val message: String): TaskDetailUiChannel()
        data class RunActions(val actions: List<TaskManager.Action>): TaskDetailUiChannel()
    }


}