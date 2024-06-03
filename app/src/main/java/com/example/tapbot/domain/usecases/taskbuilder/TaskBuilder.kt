package com.example.tapbot.domain.usecases.taskbuilder

import com.example.tapbot.domain.model.Action
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup
import java.util.UUID

class TaskBuilder(taskGroup: TaskGroup?) {
    private var taskManager: TaskManager
    private var tasGroupId: String = ""

    private var startLoopCount: Int = 0
    private var lastTask: Task? = null

    init {
        if (taskGroup == null) {
            taskManager = TaskManager()
            tasGroupId = UUID.randomUUID().toString()
        }else {
            taskManager = TaskManager()
            tasGroupId = taskGroup.taskGroupId
        }
    }

    fun getTaskGroup(): TaskGroup {
        val taskGroup = TaskGroup(taskGroupId = tasGroupId)
        return taskGroup
    }

    fun pushOldTask(tasks: List<Task>) {
        startLoopCount = tasks.filterIsInstance<StartLoop>().size
        lastTask = tasks.last()
        taskManager.pushOldTask(tasks)
    }


    fun builder(): TaskBuilder {
        return this
    }

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

    fun build(): List<Task> {
        taskManager.buildTasks()
        return taskManager.getTasks()
    }

    fun getActions(): List<Action> {
        taskManager.buildTasks().also { return taskManager.buildAction() }
    }

    fun getTempTaskList(): List<Task> {
        return taskManager.getTasks()
    }

    fun click(): TaskBuilder {
        taskManager.addTask(ClickTask(taskGroupId = tasGroupId).also { lastTask = it })
            .also { return this }
    }

    fun delay(): TaskBuilder {
        taskManager.addTask(DelayTask(taskGroupId = tasGroupId).also { lastTask = it })
        return this
    }

    fun stopLoop(): TaskBuilder {
        if (startLoopCount == 0) throw TaskManagerError("No loop to stop")
        if (lastTask is StopLoop || lastTask is StartLoop) throw TaskManagerError("Stop loop should not be called after another stop or start loop")
        taskManager.addTask(StopLoop(taskGroupId = tasGroupId).also { lastTask = it })
        return this
    }

    fun loop(): TaskBuilder {
        taskManager.addTask(StartLoop(taskGroupId = tasGroupId)
                .also { lastTask = it; startLoopCount++ })
        return this
    }


}


fun main() {
    val tasks = TaskBuilder(null).builder()
        .click().click().delay()
        .loop().click().delay().stopLoop()
        .loop().click().delay().stopLoop()
        .delay().click().getActions()

    //tasks.runTask()

    tasks.forEach {
        println(it)
    }
}


