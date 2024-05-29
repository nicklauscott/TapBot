package com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events

import com.example.tapbot.domain.model.Actions
import com.example.tapbot.domain.model.Task

sealed class TaskDetailScreenUiEvent {
    data class AddAction(val action: Actions) : TaskDetailScreenUiEvent()
    data class DeleteAction(val taskIndex: Int) : TaskDetailScreenUiEvent()
    data class EditAction(val index: Int, val task: Task) : TaskDetailScreenUiEvent()
    object SaveTask: TaskDetailScreenUiEvent()
    object PlayTask: TaskDetailScreenUiEvent()
}