package com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events

import android.content.Context
import com.example.tapbot.domain.model.Actions
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup

sealed class TaskDetailScreenUiEvent {
    data class AddAction(val action: Actions) : TaskDetailScreenUiEvent()
    data class DeleteAction(val taskIndex: Int) : TaskDetailScreenUiEvent()
    data class EditAction(val index: Int, val task: Task) : TaskDetailScreenUiEvent()
    class CompleteTask(val name: String, val description: String?) : TaskDetailScreenUiEvent()
    data class ReOrder(val from: Int, val to: Int) : TaskDetailScreenUiEvent()

    object SaveTask: TaskDetailScreenUiEvent()
    object ToggleFavorite: TaskDetailScreenUiEvent()
    object DeleteTask: TaskDetailScreenUiEvent()
    object PlayTask: TaskDetailScreenUiEvent()
    object StopOldAndStartNewTask: TaskDetailScreenUiEvent()
}