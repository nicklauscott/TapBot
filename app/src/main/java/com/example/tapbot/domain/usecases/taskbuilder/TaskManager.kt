package com.example.tapbot.domain.usecases.taskbuilder

import android.util.Log
import com.example.tapbot.domain.model.Action
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.IndividualTask
import com.example.tapbot.domain.model.LoopTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task

class TaskManagerError(message: String) : Exception(message)
class TaskManagerWarning(message: String) : Exception(message)

class TaskManager {

    private var loopCount = 0
    private var stopLoopCount = 0

    private val tasks = mutableListOf<Task>()

    fun addTask(task: Task) {
        if (task is StartLoop) loopCount++

        if (task is StopLoop) {
            if (stopLoopCount + 1 > loopCount) throw TaskManagerError("This wont cancel anything")
            stopLoopCount++

            var loopIndex = 0
            tasks.forEachIndexed { index, it ->
                if (it is StartLoop) { loopIndex = index }
            }
            val loopId = (tasks[loopIndex] as StartLoop).id

            tasks.add(task.copy(prentLoopId = loopId))
            return
        }

        tasks.add(task)
    }

    fun editTask(index: Int, task: Task) {
        tasks.removeAt(index)
        tasks.add(index, task)
    }

    fun deleteTask(index: Int)  {
        if (tasks[index] is StartLoop) loopCount--
        if (tasks[index] is StopLoop) stopLoopCount--

        tasks.removeAt(index)
    }

    fun canDeleteTask(index: Int): Boolean {
        if (tasks[index] is StartLoop && loopCount - 1 < stopLoopCount)
            throw TaskManagerError("Can't delete a loop without deleting the stop loop")

        if (tasks[index] is StopLoop && stopLoopCount - 1 < loopCount)
            throw TaskManagerWarning("Removing this might lead to an infinite loop")


        val behind = if (index > 0) tasks[index - 1] else null
        val ahead = if (index < tasks.size - 1) tasks[index + 1] else null
        if (behind is StartLoop && ahead is StopLoop) throw
        TaskManagerError("Loop and StopLoop can't be directly together")

        return true
    }

    fun getTasks(): List<Task> {
        return tasks
    }

    fun buildTasks() {
        val tempTasks = mutableListOf<Task>()
        tasks.forEachIndexed { index, task ->
            when (task) {
                is ClickTask -> tempTasks.add(task.copy(taskNumberInTheList = index))
                is DelayTask -> tempTasks.add(task.copy(taskNumberInTheList = index))
                is StartLoop -> tempTasks.add(task.copy(taskNumberInTheList = index))
                is StopLoop -> tempTasks.add(task.copy(taskNumberInTheList = index))
            }
        }
        tasks.clear()
        tasks.addAll(tempTasks)
    }


    fun buildAction(): List<Action> { // gpt
        val actions = mutableListOf<Action>()

        // get looped tasks
        val startAndStopLoopIndex = mutableListOf<List<Int>>() // [startLoopIndex, stopLoopIndex]
        var startLoop = -1
        tasks.forEachIndexed { index, task ->
            if (task is StartLoop) startLoop = index // add the index of the start loop
            if (task is StopLoop) {
                startAndStopLoopIndex.add(listOf(startLoop, index)) // add the start and stop loop index to the list
                startLoop = -1 // reset the start loop index
            }
        }

        var currentIndex = 0
        if (startAndStopLoopIndex.isNotEmpty()) {
            startAndStopLoopIndex.forEach { (start, stop) ->
                // Add individual tasks before the current loop
                if (currentIndex < start) {
                    tasks.subList(currentIndex, start).forEach { task ->
                        actions.add(IndividualTask(task))
                    }
                }

                // Add loop tasks
                val loopTasks = tasks.subList(start, stop + 1)
                actions.add(LoopTask(loopTasks))
                currentIndex = stop + 1
            }

            // Add remaining individual tasks after the last loop
            if (currentIndex < tasks.size) {
                tasks.subList(currentIndex, tasks.size).forEach { task ->
                    actions.add(IndividualTask(task))
                }
            }
        } else {
            // If there are no loops, add all tasks as individual tasks
            tasks.forEach { task ->
                actions.add(IndividualTask(task))
            }
        }
        return actions
    }

    fun pushOldTask(tasks: List<Task>) {
        this.tasks.clear()
        this.tasks.addAll(tasks)
        loopCount = this.tasks.count { it is StartLoop }
        stopLoopCount = this.tasks.count { it is StopLoop }
    }

}
