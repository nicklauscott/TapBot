package com.example.tapbot.domain.usecases.taskbuilder

import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task

class TaskManager {

    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        when (task) {
            is ClickTask -> arrangeClickTask(task)
            is DelayTask -> arrangeDelayTask(task)
            is StopLoop -> arrangeStopLoopTask(task)
            is StartLoop -> arrangeStartLoopTask(task)
        }
    }

    fun editTask(index: Int, task: Task) {
        tasks.removeAt(index)
        tasks.add(index, task)
    }

    fun deleteTask(index: Int): List<Task> {
        tasks.removeAt(index)
        return tasks
    }

    fun getTasks(): List<Task> {
        return tasks
    }

    fun buildTasks() {
        val loops = tasks.map { child ->  //  List<Pair<index in tasks list, childrenIds>>
            val index = tasks.indexOf(child)
            val childrenIds = mutableListOf<String>()
            if (child is StartLoop) {
                val clicks = tasks.filterIsInstance<ClickTask>().filter { click -> click.parentTaskId == child.id }.map { it.id to (it.childIdIndexId ?: 1) }
                val delays = tasks.filterIsInstance<DelayTask>().filter { delay -> delay.parentTaskId == child.id }.map { it.id to (it.childIdIndexId ?: 1) }
                val stops = tasks.filterIsInstance<StopLoop>().filter { stop -> stop.parentTaskId == child.id }.map { it.id to (it.childIdIndexId ?: 1) }
                (clicks + delays + stops).sortedBy { it.second }.forEach { childrenIds.add(it.first) }
            }
            index to childrenIds
        }

        loops.forEach {
            if (tasks[it.first] is StartLoop) {
                val oldLoopTask = tasks[it.first] as StartLoop
                val newLoopTask = StartLoop(
                    id = oldLoopTask.id, tasGroupId = oldLoopTask.tasGroupId,
                    taskNumberInTheList = oldLoopTask.taskNumberInTheList,
                    standaloneTask = oldLoopTask.standaloneTask, parentTaskId = oldLoopTask.parentTaskId,
                    childIdIndexId = oldLoopTask.childIdIndexId, childrenTaskIds = it.second
                )
                tasks.removeAt(it.first)
                tasks.add(it.first, newLoopTask)
            }
        }

    }

    private fun arrangeStartLoopTask(task: StartLoop) {
        val taskNumberInTheList: Int = if (tasks.isEmpty()) 1 else tasks.size

        if (tasks.isEmpty()) {
            task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = true,
                parentTaskId = null, childIdIndexId = null)
                .also { tasks.add(it) }
            return
        }

        if (task.standaloneTask) {
            task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = null, childIdIndexId = null)
                .also { tasks.add(it) }
            return
        }

        when (val lastTask = tasks.last()) {
            is ClickTask -> {
                task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = false,
                    parentTaskId = lastTask.parentTaskId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is DelayTask -> {
                task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = false,
                    parentTaskId = lastTask.parentTaskId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is StopLoop -> {
                // Skip the last StartLoop task and take the previous if any; because this last StopLoop would've cancelled it
                val loopTasks = tasks.filterIsInstance<StartLoop>()
                val lastTaskParentId = if (loopTasks.size > 1) loopTasks[loopTasks.size - 2] else null

                // Tasks belonging to the previous StartLoop
                val clicks = tasks.filterIsInstance<ClickTask>().filter { it.parentTaskId == lastTaskParentId?.id }.size
                val delays = tasks.filterIsInstance<DelayTask>().filter { it.parentTaskId == lastTaskParentId?.id }.size
                val loops = tasks.filterIsInstance<StartLoop>().filter { it.parentTaskId == lastTaskParentId?.id }.size

                task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = lastTaskParentId?.id,
                    childIdIndexId = clicks + delays + loops + 1)
                    .also { tasks.add(it) }
            }
            is StartLoop -> {
                task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = lastTask.parentTaskId,
                    childIdIndexId = 1)
                    .also { tasks.add(it) }
            }
        }

    }

    private fun arrangeStopLoopTask(task: StopLoop) {
        val taskNumberInTheList: Int = if (tasks.isEmpty()) 1 else tasks.size

        when (val lastTask = tasks.last()) {
            is ClickTask -> {
                val lastTaskParentId = lastTask.parentTaskId
                task.copy(taskNumberInTheList = taskNumberInTheList,
                    parentTaskId = lastTaskParentId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is DelayTask -> {
                val lastTaskParentId = lastTask.parentTaskId
                task.copy(taskNumberInTheList = taskNumberInTheList,
                    parentTaskId = lastTaskParentId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            else -> {
                /*
                    This should not happen
                    StopLoop: Error. nothing for this stop loop to cancel
                    StartLoop: Since the start loop is the last task we have nothing to cancel
                */
            }
        }
    }

    private fun arrangeDelayTask(task: DelayTask) {
        val taskNumberInTheList: Int = if (tasks.isEmpty()) 1 else tasks.size

        if (tasks.isEmpty()) {
            task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = true,
                parentTaskId = null, childIdIndexId = null)
                .also { tasks.add(it) }
            return
        }

        if (task.standaloneTask) {
            task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = null, childIdIndexId = null)
                .also { tasks.add(it) }
            return
        }

        when (val lastTask = tasks.last()) {
            is ClickTask -> {
                val lastTaskParentId = lastTask.parentTaskId
                task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = false,
                    parentTaskId = lastTaskParentId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is DelayTask -> {
                val lastTaskParentId = lastTask.parentTaskId
                task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = false,
                    parentTaskId = lastTaskParentId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is StopLoop -> {
                // Skip the last StartLoop task and take the previous if any; because this last StopLoop would've cancelled it
                val loopTasks = tasks.filterIsInstance<StartLoop>()
                val lastTaskParentId = if (loopTasks.size > 1) loopTasks[loopTasks.size - 2] else null

                // Tasks belonging to the previous StartLoop
                val clicks = tasks.filterIsInstance<ClickTask>().filter { it.parentTaskId == lastTaskParentId?.id }.size
                val delays = tasks.filterIsInstance<DelayTask>().filter { it.parentTaskId == lastTaskParentId?.id }.size
                val loops = tasks.filterIsInstance<StartLoop>().filter { it.parentTaskId == lastTaskParentId?.id }.size

                task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = lastTaskParentId?.id,
                    childIdIndexId = clicks + delays + loops + 1)
                    .also { tasks.add(it) }
            }
            is StartLoop -> {
                // Since the start loop is the last task we can now add the new delay task as it's child
                task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = lastTask.id,
                    childIdIndexId = 1)
                    .also { tasks.add(it) }
            }
        }
    }

    private fun arrangeClickTask(task: ClickTask) {
        val taskNumberInTheList: Int = if (tasks.isEmpty()) 1 else tasks.size

        // if first task
        if (tasks.isEmpty()) {
            task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = true,
                parentTaskId = null, childIdIndexId = null)
                .also { tasks.add(it) }
            return
        }

        // if standalone
        if (task.standaloneTask) {
            task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = null, childIdIndexId = null)
                .also { tasks.add(it) }
            return
        }

        // if not standalone
        when (val lastTask = tasks.last()) {
            is ClickTask -> {
                val lastTaskParentId = lastTask.parentTaskId
                task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = false,
                    parentTaskId = lastTaskParentId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is DelayTask -> {
                val lastTaskParentId = lastTask.parentTaskId
                task.copy(taskNumberInTheList = taskNumberInTheList, standaloneTask = false,
                    parentTaskId = lastTaskParentId, childIdIndexId = lastTask.childIdIndexId?.plus(1))
                    .also { tasks.add(it) }
            }
            is StopLoop -> {
                // Skip the last StartLoop task and take the previous if any; because this last StopLoop would've cancelled it
                val loopTasks = tasks.filterIsInstance<StartLoop>()
                val lastTaskParentId = if (loopTasks.size > 1) loopTasks[loopTasks.size - 2] else null

                // Tasks belonging to the previous StartLoop
                val clicks = tasks.filterIsInstance<ClickTask>().filter { it.parentTaskId == lastTaskParentId?.id }.size
                val delays = tasks.filterIsInstance<DelayTask>().filter { it.parentTaskId == lastTaskParentId?.id }.size
                val loops = tasks.filterIsInstance<StartLoop>().filter { it.parentTaskId == lastTaskParentId?.id }.size

                task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = lastTaskParentId?.id,
                    childIdIndexId = clicks + delays + loops + 1)
                    .also { tasks.add(it) }
            }
            is StartLoop -> {
                // Since the start loop is the last task we can now add the new task as it's child
                task.copy(taskNumberInTheList = taskNumberInTheList, parentTaskId = lastTask.id,
                    childIdIndexId = 1)
                    .also { tasks.add(it) }
            }
        }
    }

}
