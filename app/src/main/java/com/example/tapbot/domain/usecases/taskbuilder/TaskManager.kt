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


    interface Action

    data class IndividualTask( // just run the task
        val task: Task,
    ): Action

    data class LoopTask( // loop through the tasks and run it
        val tasks: List<IndividualTask>,
    ): Action
    fun buildAction(): List<Action> {
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
        // startAndStopLoopIndex = [ [5, 9], [13, 18] ]

        // lets add the task before the first start loop if any
        val firstStartLoopIndex = if (startAndStopLoopIndex.isNotEmpty()) startAndStopLoopIndex[0][0] else null
        if (firstStartLoopIndex != null && firstStartLoopIndex > 0) {
            tasks.subList(0, firstStartLoopIndex).forEachIndexed { _, task -> // take the first part of the list before start loop
                actions.add(IndividualTask(task))  // add the to the list of actions
            }
        } else {
            tasks.forEachIndexed { _, task -> // take the first part of the list before start loop
                actions.add(IndividualTask(task))  // add the to the list of actions
            }
        }

        if (startAndStopLoopIndex.isNotEmpty()) {
            // Now we go through the loop tasks
            var finished = startAndStopLoopIndex[0][0] - 1 // set the index of the finished task
            startAndStopLoopIndex.forEach { loopTaskIndex ->

                if (finished + 1 == loopTaskIndex[0]) { // then the next task is a start loop

                    val tempLoopTasks = mutableListOf<IndividualTask>()

                    tasks.subList(loopTaskIndex[0], loopTaskIndex[1] + 1).forEach { task ->
                        tempLoopTasks.add(IndividualTask(task)) // add the task from the loop
                    }

                    actions.add(LoopTask(tempLoopTasks)) // add the loop to the list of actions
                    finished = loopTaskIndex[1] // set the index of the last task from the loop

                }else {

                    // we do the tasks that's in between a loop
                    tasks.subList(finished + 1, loopTaskIndex[0]).forEach { task ->
                        actions.add(IndividualTask(task))
                    }

                    // we now do the tasks in the current loop; by do the same thing we did above
                    val tempLoopTasks = mutableListOf<IndividualTask>()

                    tasks.subList(loopTaskIndex[0], loopTaskIndex[1] + 1).forEach { task ->
                        tempLoopTasks.add(IndividualTask(task))
                    }

                    actions.add(LoopTask(tempLoopTasks))
                    finished = loopTaskIndex[1]

                }

            }


            // add the last task
            val lastStartLoopIndex = startAndStopLoopIndex[startAndStopLoopIndex.size - 1][1] + 1
            tasks.subList(lastStartLoopIndex, tasks.size).forEach { task ->
                actions.add(IndividualTask(task))
            }
        }


        return actions

    }

}
