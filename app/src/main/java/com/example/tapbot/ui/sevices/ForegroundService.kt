package com.example.tapbot.ui.sevices

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoopCondition
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.usecases.taskbuilder.TaskManager
import com.example.tapbot.domain.utils.triggerClick
import kotlinx.coroutines.delay

object ForegroundService {
    fun startService(context: Context): Boolean {
        val serviceIntent = Intent(context, TapBotForegroundService::class.java)
        context.startService(serviceIntent)
        return checkServiceRunning(context)
    }

    fun stopService(context: Context): Boolean {
        val serviceIntent = Intent(context, TapBotForegroundService::class.java)
        context.stopService(serviceIntent)
        return !checkServiceRunning(context)
    }

    @SuppressLint("ServiceCast")
    fun checkServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (TapBotForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }


    suspend fun Context.runTask(actions: List<TaskManager.Action>) {

        actions.forEach { action ->
            if (action is TaskManager.IndividualTask) {
                if (action.task is ClickTask) {
                    action.task.delayBeforeTask?.let { delayTime -> delay(delayTime.toLong()) }
                    repeat(action.task.clickCount) { _ ->
                        triggerClick(action.task.x, action.task.y)
                        delay(action.task.delayBetweenClicks.toLong())
                    }
                }

                if (action.task is DelayTask) {  delay(action.task.getDelay()) }
            }

            if (action is TaskManager.LoopTask) {
                var currentLoopStartTime = System.currentTimeMillis()
                var initializedTime = false
                var currentIteration = 0
                var keepRunning = true

                while (keepRunning) {

                    Log.d("TaskDetailViewModel", "play 7------")

                    action.tasks.forEach { eachAction ->

                        if (eachAction.task is StartLoop) {
                            if (eachAction.task.enableCountCondition) currentIteration++

                            if (eachAction.task.enableTimeCondition) {
                                if (!initializedTime) currentLoopStartTime = System.currentTimeMillis()
                                initializedTime = true
                            }
                        }

                        if (eachAction.task is ClickTask) {
                            Log.d("TaskDetailViewModel", "play 9")
                            eachAction.task.delayBeforeTask?.let { delayTime -> delay(delayTime.toLong()) }
                            repeat(eachAction.task.clickCount) { _ ->
                                triggerClick(eachAction.task.x, eachAction.task.y)
                                delay(eachAction.task.delayBetweenClicks.toLong())
                            }
                        }

                        if (eachAction.task is DelayTask) { delay(eachAction.task.getDelay()) }

                        if (eachAction.task is StopLoop) {

                            val stoopLooCondition = StopLoopCondition // initialize the the checker class

                            // get the time condition
                            val timeCondition = eachAction.task.useOneCondition?.firstConditionOperator?.let {
                                stoopLooCondition.check(time = eachAction.task.time, operator = it, loopTime = currentLoopStartTime)
                            }

                            // get the count condition
                            val countCondition = eachAction.task.useOneCondition?.secondConditionOperator?.let {
                                stoopLooCondition.check(count = eachAction.task.count, operator = it, loopCount = currentIteration)
                            }

                            // check the time and count condition
                            if (eachAction.task.enableTimeCondition && eachAction.task.enableTimeCondition) {
                                if (timeCondition != null && countCondition != null) {
                                    val comparator = StopLoopCondition.Comparator(timeCondition,
                                        eachAction.task.useOneCondition.joiners, countCondition)

                                    if (stoopLooCondition.check(comparator)) keepRunning = false
                                }
                            }

                            // check the time condition
                            if (eachAction.task.enableTimeCondition) { if (timeCondition != null && timeCondition) keepRunning = false }

                            // check the count condition
                            if (eachAction.task.enableCountCondition) { if (countCondition == true) keepRunning = false }
                        }
                    }
                }
            }
        }
    }

}