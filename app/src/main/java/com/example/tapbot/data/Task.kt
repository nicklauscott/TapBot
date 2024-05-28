package com.example.tapbot.data

import com.example.tapbot.data.TaskBuilder.showTask
import java.util.UUID

sealed class Task

data class ClickTask(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val standaloneTask: Boolean = true,
    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,

    val delayBeforeTask: Int? = 3,
    val x: Float = 500f,
    val y: Float = 700f,
    val delayBetweenClicks: Int = 1,
    val clickCount: Int = 1
): Task()


data class DelayTask(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val standaloneTask: Boolean = true,
    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,

    val delay: Int = 1
): Task()

data class RepeatTask(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val standaloneTask: Boolean = true,
    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,

    val repeatCount: Int = 1
)


data class StartLoop(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val standaloneTask: Boolean = true,
    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,

    val childrenTaskIds: List<String> = emptyList()
): Task()

data class StopLoop(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,
): Task()


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



object TaskBuilder {
    private lateinit var taskManager: TaskManager
    private var tasGroupId: String = ""

    private var tasksCount: Int = 0
    private var isLooping: Boolean = false
    private var startLoopCount: Int = 0
    private var lastTask: Task? = null

    fun builder(): TaskBuilder {
        taskManager = TaskManager()
        tasGroupId = UUID.randomUUID().toString()
        return this
    }

    fun build(): List<Task> {
        taskManager.buildTasks()
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
                childIdIndexId = childIdIndexId, delay = delay).also { lastTask = it; tasksCount++ }
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
        taskManager.addTask(StartLoop(
            tasGroupId = tasGroupId,
            standaloneTask = standaloneTask ?: true, parentTaskId = parentTaskId,
            childIdIndexId = childIdIndexId
        ).also { lastTask = it; startLoopCount++; tasksCount++; isLooping = true })
        return this
    }

    fun List<Task>.showTask() {
        val list = this.toMutableList()

        // get first task
        val tasksByGroup = mutableMapOf<String, List<Task>>()

        this.forEach { task ->
            if (task is ClickTask && task.standaloneTask) { tasksByGroup[task.id] = listOf(task) }

            if (task is DelayTask && task.standaloneTask) { tasksByGroup[task.id] = listOf(task) }

            if (task is StartLoop && task.standaloneTask) {

                val childrenIds = mutableListOf<String>()
                val clicks = list.filterIsInstance<ClickTask>().filter { click -> click.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
                val delays = list.filterIsInstance<DelayTask>().filter { delay -> delay.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
                val stops = list.filterIsInstance<StopLoop>().filter { stop -> stop.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
                val starts = list.filterIsInstance<StartLoop>().filter { start -> start.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
                (clicks + delays + stops + starts).sortedBy { it.second }.forEach { childrenIds.add(it.first) }

                val getTasks = mutableListOf<Task>()
                childrenIds.forEach { id ->
                    this.forEach {
                        if (it is ClickTask && it.id == id) getTasks.add(it)
                        if (it is DelayTask && it.id == id) getTasks.add(it)
                        if (it is StopLoop && it.id == id) getTasks.add(it)
                        if (it is StartLoop && it.id == id) getTasks.add(it)
                        list.remove(it)
                    }
                }
                tasksByGroup[task.id] = listOf(task) + getTasks

            }

//            if (task is StartLoop && !task.standaloneTask) {
//                println("Here")
//                // find its parent start loop in the task by group and add to it
//                val parentLoop = tasksByGroup[task.parentTaskId]
//
//                if (parentLoop != null) {
//
//                    val childrenIds = mutableListOf<String>()
//                    val clicks = list.filterIsInstance<ClickTask>().filter { click -> click.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
//                    val delays = list.filterIsInstance<DelayTask>().filter { delay -> delay.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
//                    val stops = list.filterIsInstance<StopLoop>().filter { stop -> stop.parentTaskId == task.id }.map { it.id to (it.childIdIndexId ?: 1) }
//                    (clicks + delays + stops).sortedBy { it.second }.forEach { childrenIds.add(it.first) }
//
//                    val getTasks = mutableListOf<Task>()
//                    childrenIds.forEach { id ->
//                        this.forEach {
//                            if (it is ClickTask && it.id == id) getTasks.add(it)
//                            if (it is DelayTask && it.id == id) getTasks.add(it)
//                            if (it is StopLoop && it.id == id) getTasks.add(it)
//                            list.remove(it)
//                        }
//                    }
//
//                    println("GetTasks: ${list.filterIsInstance<ClickTask>().filter { click -> click.parentTaskId == task.id }}")
//
//                    // how can i add this to tasksByGroup and maintain the order
////                    val parentTasks = parentLoop[task.parentTaskId]
////                    if (parentTasks != null) {
////                        val newTaskList = parentTasks + listOf(task) + getTasks
////                        parentLoop[task.id] to newTaskList
////                    }
//                }
//
//            }
        }

        tasksByGroup.forEach { (_, value) ->
            var indent = ""
            var starLoop = 0
            value.forEach { task ->
                if (task is StartLoop) {
                    println(task)
                    starLoop++
                    indent += "     "
                }
                if (task is StopLoop) {
                    indent = indent.removeRange(0..4)
                    println(task)
                }
                else println("$indent $task")
            }
            indent = ""
        }
    }
}




fun main() {
    val tasks = TaskBuilder.builder()
        .loop(standaloneTask = true)
            .click(standaloneTask = false)
            .click(standaloneTask = false)
        .stopLoop()
        .delay(standaloneTask = false, delay = 1)
        .stopLoop()
        .build()


    tasks.forEach { println(it) }
    //tasks.showTask()
}


/*

    1. click a point
    2. delay
    3. click another point
    4. start loop --
        4a delay
        4b click a point
    5. end loop
    6. click another point
    7. repeat

 */
