package com.example.tapbot.domain.usecases.taskbuilder

import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task
import java.util.UUID

class TaskBuilder {
    private lateinit var taskManager: TaskManager
    private var tasGroupId: String = ""

    private var startLoopCount: Int = 0
    private var lastTask: Task? = null

    fun deleteTask(index: Int) {
        if (taskManager.getTasks()[index] is StopLoop) lastTask = null
        taskManager.deleteTask(index)
    }

    fun canDeleteTask(index: Int): Boolean {
        return taskManager.canDeleteTask(index)
    }

    fun editTask(index: Int, task: Task) {
        taskManager.editTask(index, task)
    }

    fun builder(): TaskBuilder {
        taskManager = TaskManager()
        tasGroupId = UUID.randomUUID().toString()
        return this
    }

    fun build(): List<Task> {
        taskManager.buildTasks()
        return taskManager.getTasks()
    }

    fun getTempTaskList(): List<Task> {
        return taskManager.getTasks()
    }

    fun click(): TaskBuilder {
        taskManager.addTask(ClickTask(tasGroupId = tasGroupId).also { lastTask = it })
            .also { return this }
    }

    fun delay(): TaskBuilder {
        taskManager.addTask(DelayTask(tasGroupId = tasGroupId).also { lastTask = it })
        return this
    }

    fun stopLoop(): TaskBuilder {
        if (startLoopCount == 0) throw TaskManagerError("No loop to stop")
        if (lastTask is StopLoop || lastTask is StartLoop) throw TaskManagerError("Stop loop should not be called after another stop or start loop")
        taskManager.addTask(StopLoop(tasGroupId = tasGroupId).also { lastTask = it })
        return this
    }

    fun loop(): TaskBuilder {
        taskManager.addTask(StartLoop(tasGroupId = tasGroupId)
                .also { lastTask = it; startLoopCount++ })
        return this
    }

}


