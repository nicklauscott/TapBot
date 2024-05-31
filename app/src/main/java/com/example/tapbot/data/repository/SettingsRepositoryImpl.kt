package com.example.tapbot.data.repository

import com.example.tapbot.data.database.TaskDatabase
import com.example.tapbot.data.mapper.toClickTask
import com.example.tapbot.data.mapper.toDelayTask
import com.example.tapbot.data.mapper.toStartLoop
import com.example.tapbot.data.mapper.toStopLoop
import com.example.tapbot.data.mapper.toTaskGroup
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.Task
import com.example.tapbot.domain.model.TaskGroup
import com.example.tapbot.domain.model.TaskGroupList
import com.example.tapbot.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
class TasksRepositoryImpl @Inject constructor(
    private val tasksDatabase: TaskDatabase
): TasksRepository {
    override suspend fun getAllTasksGroup(): Flow<List<TaskGroup>> {
        return tasksDatabase.taskDao().getAllTaskGroup().map { list -> list.map { it.toTaskGroup() } }
    }

    override suspend fun searchTasksGroup(query: String): Flow<List<TaskGroup>> {
        return tasksDatabase.taskDao().getTaskGroupWithName(query).map { list -> list.map { it.toTaskGroup() } }
    }

    override suspend fun getFavoriteTasksGroup(): Flow<List<TaskGroup>> {
        return tasksDatabase.taskDao().getTFavoriteTaskGroup().map { list -> list.map { it.toTaskGroup() } }
    }

    override suspend fun getTasksGroupById(tasksGroupId: String): Flow<TaskGroupList> {
        return flow {
            val taskGroup = tasksDatabase.taskDao().getTaskGroupWithId(tasksGroupId)?.toTaskGroup()
            val clickTasks = tasksDatabase.taskDao().getClickTasksWithTaskGroupId(tasksGroupId).map { it.toClickTask() }
            val delayTasks = tasksDatabase.taskDao().getDelayTasksWithTaskGroupId(tasksGroupId).map { it.toDelayTask() }
            val startLoopTasks = tasksDatabase.taskDao().getStartLoopTasksWithTaskGroupId(tasksGroupId).map { it.toStartLoop() }
            val stopLoopTasks = tasksDatabase.taskDao().getStopLoopTasksWithTaskGroupId(tasksGroupId).map { it.toStopLoop() }
            emit(TaskGroupList(taskGroup, clickTasks + delayTasks + startLoopTasks + stopLoopTasks))
        }
    }

    override suspend fun saveTasksGroup(tasksGroup: TaskGroup, tasks: List<Task>) {
        tasksDatabase.taskDao().insertTaskGroup(tasksGroup.toTaskGroup())
        tasks.forEach {
            if (it is ClickTask) tasksDatabase.taskDao().insertClickTask(it.toClickTask())
            if (it is DelayTask) tasksDatabase.taskDao().insertDelayTask(it.toDelayTask())
            if (it is StartLoop) tasksDatabase.taskDao().insertStartLoop(it.toStartLoop())
            if (it is StopLoop) tasksDatabase.taskDao().insertStopLoop(it.toStopLoop())
        }
    }

    override suspend fun deleteTasksGroup(tasksGroupId: String) {
        tasksDatabase.taskDao().deleteTaskGroupWithId(tasksGroupId)
        tasksDatabase.taskDao().deleteClickTasksWithTaskGroupId(tasksGroupId)
        tasksDatabase.taskDao().deleteDelayTasksWithTaskGroupId(tasksGroupId)
        tasksDatabase.taskDao().deleteStartLoopTasksWithTaskGroupId(tasksGroupId)
        tasksDatabase.taskDao().deleteStopLoopTasksWithTaskGroupId(tasksGroupId)
    }

}