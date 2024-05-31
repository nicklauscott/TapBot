package com.example.tapbot.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tapbot.data.database.entities.ClickTask
import com.example.tapbot.data.database.entities.DelayTask
import com.example.tapbot.data.database.entities.StartLoop
import com.example.tapbot.data.database.entities.StopLoop
import com.example.tapbot.data.database.entities.TaskGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // insert all tasks one by one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClickTask(clickTask: ClickTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelayTask(delayTask: DelayTask)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStartLoop(startLoop: StartLoop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStopLoop(stopLoop: StopLoop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskGroup(taskGroup: TaskGroup)


    // get all tasks one by one
    @Query("SELECT * FROM clicktask WHERE taskGroupId = :taskGroupId")
    suspend fun getClickTasksWithTaskGroupId(taskGroupId: String): List<ClickTask>

    @Query("SELECT * FROM delaytask WHERE taskGroupId = :taskGroupId")
    suspend fun getDelayTasksWithTaskGroupId(taskGroupId: String): List<DelayTask>

    @Query("SELECT * FROM startloop WHERE taskGroupId = :taskGroupId")
    suspend fun getStartLoopTasksWithTaskGroupId(taskGroupId: String): List<StartLoop>

    @Query("SELECT * FROM stoploop WHERE taskGroupId = :taskGroupId")
    suspend fun getStopLoopTasksWithTaskGroupId(taskGroupId: String): List<StopLoop>

    @Query("SELECT * FROM taskgroup WHERE taskGroupId = :taskGroupId")
    suspend fun getTaskGroupWithId(taskGroupId: String): TaskGroup?

    @Query("SELECT * FROM taskgroup WHERE name LIKE '%' || :name || '%'")
    fun getTaskGroupWithName(name: String): Flow<List<TaskGroup>>

    @Query("SELECT * FROM taskgroup WHERE favorite = 1")
    fun getTFavoriteTaskGroup(): Flow<List<TaskGroup>>

    @Query("SELECT * FROM taskgroup")
    fun getAllTaskGroup(): Flow<List<TaskGroup>>


    // delete all tasks one by one
    @Query("DELETE FROM clicktask WHERE taskGroupId = :taskGroupId")
    suspend fun deleteClickTasksWithTaskGroupId(taskGroupId: String)

    @Query("DELETE FROM delaytask WHERE taskGroupId = :taskGroupId")
    suspend fun deleteDelayTasksWithTaskGroupId(taskGroupId: String)

    @Query("DELETE FROM startloop WHERE taskGroupId = :taskGroupId")
    suspend fun deleteStartLoopTasksWithTaskGroupId(taskGroupId: String)

    @Query("DELETE FROM stoploop WHERE taskGroupId = :taskGroupId")
    suspend fun deleteStopLoopTasksWithTaskGroupId(taskGroupId: String)

    @Query("DELETE FROM taskgroup WHERE taskGroupId = :taskGroupId")
    suspend fun deleteTaskGroupWithId(taskGroupId: String)

}