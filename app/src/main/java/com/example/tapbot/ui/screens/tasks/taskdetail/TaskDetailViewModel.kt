package com.example.tapbot.ui.screens.tasks.taskdetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapbot.domain.model.AccessibilityServiceNotEnabledException
import com.example.tapbot.domain.model.Actions
import com.example.tapbot.domain.model.ServiceState
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskAlreadyRunningException
import com.example.tapbot.domain.usecases.DeleteActions
import com.example.tapbot.domain.usecases.GetTaskGroupAction
import com.example.tapbot.domain.usecases.SaveActions
import com.example.tapbot.domain.usecases.service.TapService
import com.example.tapbot.domain.usecases.taskbuilder.TaskBuilder
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
    private val tapService: TapService,
    saveStateHandle: SavedStateHandle
): ViewModel() {

    private lateinit var taskBuilder: TaskBuilder

    private val _state: MutableState<TaskDetailScreenState> = mutableStateOf(TaskDetailScreenState())
    val state: State<TaskDetailScreenState> = _state

    private val tempDeletedTask = mutableListOf<Task>()

    private val _serviceState: MutableState<ServiceState> = mutableStateOf(ServiceState())
    val serviceState: State<ServiceState> = _serviceState

    private val _channel: Channel<TaskDetailUiChannel> = Channel(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()


    init {
        val taskGroupId = saveStateHandle.get<String>("taskId")
        _state.value = state.value.copy(loading = true)
        viewModelScope.launch(Dispatchers.IO) {
            if (taskGroupId != "-1") {
                taskGroupId?.let { id ->
                    getTaskGroupAction(id).collect {
                        withContext(Dispatchers.Main) {
                            taskBuilder = TaskBuilder(it.taskGroup)
                            _state.value = state.value.copy(loading = false, taskGroup = it.taskGroup, taskList = it.tasks)
                            state.value.update(taskGroup = it.taskGroup, taskList = it.tasks)
                        }
                        taskBuilder.pushOldTask(state.value.taskList)
                    }
                    return@launch
                }
                return@launch
            }

            taskBuilder = TaskBuilder(null)
            val newTaskGroup = taskBuilder.getTaskGroup()
            withContext(Dispatchers.Main) {
                _state.value = TaskDetailScreenState(loading = false, taskGroup = newTaskGroup)
                state.value.update(taskGroup = newTaskGroup, taskList = state.value.taskList)
            }
        }
        viewModelScope.launch {
            tapService().collect { _serviceState.value = it }
        }
    }

    fun onEvent(event: TaskDetailScreenUiEvent) {
        when (event) {
            is TaskDetailScreenUiEvent.AddAction -> addTask(event.action)
            is TaskDetailScreenUiEvent.DeleteAction -> deleteTask(event.taskIndex)
            is TaskDetailScreenUiEvent.EditAction -> editTask(event.index, event.task)
            TaskDetailScreenUiEvent.SaveTask -> save()
            TaskDetailScreenUiEvent.PlayTask -> play()
            is TaskDetailScreenUiEvent.CompleteTask -> {
                _state.value = state.value.copy(
                    taskGroup = state.value.taskGroup?.copy(
                        name = event.name, description = event.description ?: "")
                )
                save()
            }
            TaskDetailScreenUiEvent.ToggleFavorite -> {
                _state.value = state.value.copy(
                    taskGroup = state.value.taskGroup?.copy(
                        favorite = !(state.value.taskGroup?.favorite ?: false)))
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.taskGroup?.let { saveActions(it, emptyList()) }
                }
            }
            TaskDetailScreenUiEvent.DeleteTask -> {
                if (serviceState.value.running && serviceState.value.runningTaskId == state.value.taskGroup?.taskGroupId) {
                    viewModelScope.launch {
                        _channel.send(TaskDetailUiChannel.TaskMangerError("Cannot delete running task"))
                    }
                    return
                }
                viewModelScope.launch(Dispatchers.IO) {
                    state.value.taskGroup?.let { deleteActions(it.taskGroupId) }
                    withContext(Dispatchers.Main) {
                        _channel.send(TaskDetailUiChannel.DeletedTask)
                    }
                }
            }
            TaskDetailScreenUiEvent.StopOldAndStartNewTask -> {
                tapService.stopTask()
                _serviceState.value = tapService().value
                play()
            }

            is TaskDetailScreenUiEvent.ReOrder -> reOrder(event.from, event.to)
        }
    }

    private fun play() {
        if (state.value.taskGroup?.taskGroupId == serviceState.value.runningTaskId) {
            tapService.stopTask()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val taskList = taskBuilder.getActions()
            try {
                tapService.startTask(state.value.taskGroup?.taskGroupId ?: "", taskList)
                _serviceState.value = tapService().value
            }catch (ex: TaskAlreadyRunningException) {
                _channel.send(TaskDetailUiChannel.CancelRunningTask)
            }catch (ex: AccessibilityServiceNotEnabledException) {
                _channel.send(TaskDetailUiChannel.EnableAccessibilityService)
            }
        }
    }

    private fun save() {
        if (state.value.taskGroup?.name == "") {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.CompleteTaskDetail)
            }
            return
        }
        _state.value = state.value.copy(saving = true)
        viewModelScope.launch(Dispatchers.IO) {

            // save to database
            val builtTask = taskBuilder.build()
            state.value.taskGroup?.let { saveActions(it, builtTask) }
            tempDeletedTask.forEach { deleteActions.deleteTask(it) }

            // update state
            withContext(Dispatchers.Main) {
                _state.value = state.value.copy(saving = false)
                state.value.update(taskGroup = _state.value.taskGroup, taskList = _state.value.taskList)
            }
        }
    }

    private fun reOrder(from: Int, to: Int) {
        if (from == to || from < 0 || from > state.value.taskList.size - 1
            || to < 0 || to > state.value.taskList.size - 1) return

        try {
            val reOrder = taskBuilder.canReorder(from, to)
            if (reOrder) {
                val tempList = state.value.taskList.toMutableList()
                val element = tempList.removeAt(from)
                tempList.add(to, element)

                state.value.update(taskList = tempList)
                _state.value = state.value.copy(taskList = tempList)
                taskBuilder.pushOldTask(state.value.taskList)
            }
        } catch (ex: TaskManagerError) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerError(ex.message.toString()))
            }
        }catch (ex: Exception) {
            Log.e("TaskDetailViewModel", ex.message.toString())
        }
    }


    private fun deleteTask(index: Int) {
        if (serviceState.value.running && serviceState.value.runningTaskId == state.value.taskGroup?.taskGroupId) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerError("Cannot delete running task"))
            }
            return
        }

        try {
            if (taskBuilder.canDeleteTask(index)) {
                state.value.taskList.toMutableList().also {
                    tempDeletedTask.add(it[index])
                    it.removeAt(index)
                    state.value.update(taskList = it)
                    _state.value = state.value.copy(taskList = it)
                }
                taskBuilder.deleteTask(index)
            }
        }catch (warning: TaskManagerWarning) {
            state.value.taskList.toMutableList().also {
                tempDeletedTask.add(it[index])
                it.removeAt(index)
                state.value.update(taskList = it)
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
        if (serviceState.value.running && serviceState.value.runningTaskId == state.value.taskGroup?.taskGroupId) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerError("Cannot edit running task"))
            }
            return
        }

        val newTaskList = state.value.taskList.toMutableList()
        newTaskList.removeAt(index)
        newTaskList.add(index, task)
        state.value.update(taskList = newTaskList)
        _state.value = state.value.copy(taskList = newTaskList)
        taskBuilder.editTask(index, task)
    }

    private fun addTask(task: Actions) {
        if (serviceState.value.running && serviceState.value.runningTaskId == state.value.taskGroup?.taskGroupId) {
            viewModelScope.launch {
                _channel.send(TaskDetailUiChannel.TaskMangerError("Cannot edit running task"))
            }
            return
        }

        try {
             when (task) {
                 Actions.CLICK -> taskBuilder.click()
                 Actions.DELAY -> taskBuilder.delay()
                 Actions.LOOP -> taskBuilder.loop()
                 Actions.STOP_LOOP -> taskBuilder.stopLoop()
             }
            state.value.update(taskList = taskBuilder.getTempTaskList())
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
        object CompleteTaskDetail: TaskDetailUiChannel()
        object CancelRunningTask: TaskDetailUiChannel()
        object EnableAccessibilityService: TaskDetailUiChannel()
        object DeletedTask: TaskDetailUiChannel()
    }

}