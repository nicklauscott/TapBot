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
                tasksGroup.tasks.forEach { task ->
                    if (task is ClickTask) tasks.add(task.taskNumberInTheList, task)
                    if (task is DelayTask) tasks.add(task.taskNumberInTheList, task)
                    if (task is StartLoop) tasks.add(task.taskNumberInTheList, task)
                    if (task is StopLoop) tasks.add(task.taskNumberInTheList, task)
                }
                emit(TaskGroupList(taskGroup = tasksGroup.taskGroup, tasks = tasks))
            }
        }
    }
}
