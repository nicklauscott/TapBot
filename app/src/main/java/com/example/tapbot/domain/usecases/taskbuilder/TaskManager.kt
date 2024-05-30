package com.example.tapbot.domain.usecases.taskbuilder

import android.util.Log
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
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

}
