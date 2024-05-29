package com.example.tapbot.ui.screens.tasks.taskdetail.state_and_events

import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup

data class TaskDetailScreenState(
    val loading: Boolean = false,
    val saving: Boolean = false,
    val taskGroup: TaskGroup? = null,
    val taskList: List<Task> = emptyList()
) {

    private var oldTaskGroup: TaskGroup? = null
    private var oldTaskList: List<Task> = emptyList()

    fun update(taskGroup: TaskGroup?, taskList: List<Task>) {
       oldTaskGroup = taskGroup
       oldTaskList = taskList

    }

    fun canSave(): Boolean {
        return taskGroup != oldTaskGroup || taskList != oldTaskList
    }
}