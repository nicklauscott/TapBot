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

    private var tasksCount: Int = 0
    private var isLooping: Boolean = false
    private var startLoopCount: Int = 0
    private var lastTask: Task? = null

    fun deleteTask(index: Int) = taskManager.deleteTask(index)

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

    fun click(
        standaloneTask: Boolean? = null, parentTaskId: String? = null, childIdIndexId: Int? = null,
        delayBeforeTask: Int? = null, x: Float? = null,
        y: Float? = null, delayBetweenClicks: Int? = null, clickCount: Int? = null
    ): TaskBuilder {
        if (isLooping && standaloneTask == true)
            throw Exception("Click that is standalone should come after StopLoop if StartLoop is directly above. " +
                    "\nCheck the ${tasksCount + 1} task")
        taskManager.addTask(
            ClickTask(tasGroupId = tasGroupId,
                standaloneTask = standaloneTask ?: true, parentTaskId = parentTaskId, childIdIndexId = childIdIndexId,
                delayBeforeTask = delayBeforeTask,
                x = x ?: 500f, y = y ?: 700f,
                delayBetweenClicks = delayBetweenClicks ?: 1,
                clickCount = clickCount ?: 1
            ).also { lastTask = it; tasksCount++ }
        ).also { return this }
    }

    fun delay(
        standaloneTask: Boolean? = null, parentTaskId: String? = null, childIdIndexId: Int? = null,
        delay: Int
    ): TaskBuilder {
        if (isLooping && standaloneTask == true)
            throw Exception("Delay that is standalone should come after StopLoop if StartLoop is directly above. " +
                    "\nCheck the ${tasksCount + 1} task")
        taskManager.addTask(
            DelayTask(tasGroupId = tasGroupId,
                standaloneTask = standaloneTask ?: true, parentTaskId = parentTaskId,
                childIdIndexId = childIdIndexId, delayHour = delay).also { lastTask = it; tasksCount++ }
        )
        return this
    }

    fun stopLoop(): TaskBuilder {
        if (startLoopCount == 0) throw Exception("No loop to stop")
        if (lastTask is StopLoop || lastTask is StartLoop) throw Exception("Stop loop should not be called after another stop or start loop")
        taskManager.addTask(StopLoop(tasGroupId = tasGroupId).also { lastTask = it; tasksCount++; isLooping = false })
        return this
    }

    fun loop(
        standaloneTask: Boolean? = null, parentTaskId: String? = null, childIdIndexId: Int? = null,
    ): TaskBuilder {
        taskManager.addTask(
            StartLoop(
            tasGroupId = tasGroupId,
            standaloneTask = standaloneTask ?: true, parentTaskId = parentTaskId,
            childIdIndexId = childIdIndexId
        ).also { lastTask = it; startLoopCount++; tasksCount++; isLooping = true })
        return this
    }

}