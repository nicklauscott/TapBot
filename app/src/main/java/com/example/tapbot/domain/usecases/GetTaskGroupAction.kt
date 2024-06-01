package com.example.tapbot.domain.usecases

import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroupList
import com.example.tapbot.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTaskGroupAction @Inject constructor(
    private val repository: TasksRepository
) {
    suspend operator fun invoke(tasksGroupId: String): Flow<TaskGroupList> {
        return flow {
            repository.getTasksGroupById(tasksGroupId).collect { tasksGroup ->
                val tasks = mutableListOf<Task>()
                val taskMap = mutableMapOf<Int, Task>()
                tasksGroup.tasks.forEach { task ->
                    if (task is ClickTask) taskMap[task.taskNumberInTheList] = task
                    if (task is DelayTask) taskMap[task.taskNumberInTheList] = task
                    if (task is StartLoop) taskMap[task.taskNumberInTheList] = task
                    if (task is StopLoop) taskMap[task.taskNumberInTheList] = task
                }
                taskMap.entries.sortedBy { it.key }.forEach { tasks.add(it.value) }
                emit(TaskGroupList(taskGroup = tasksGroup.taskGroup, tasks = tasks))
            }
        }
    }
}
