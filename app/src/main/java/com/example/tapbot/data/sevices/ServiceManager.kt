package com.example.tapbot.data.sevices

import android.content.Context
import android.content.Intent
import com.example.tapbot.domain.model.AccessibilityServiceNotEnabledException
import com.example.tapbot.domain.model.Action
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.IndividualTask
import com.example.tapbot.domain.model.LoopTask
import com.example.tapbot.domain.model.ServiceState
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.model.StopLoopCondition
import com.example.tapbot.domain.model.TaskAlreadyRunningException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ServiceManager(val context: Context) {

    val newContext: Context? = null

    private val _state = MutableStateFlow(ServiceState())
    val state: StateFlow<ServiceState> get() = _state.asStateFlow()
    private var job: Job? = null

    fun runTask(taskGroupId: String, actions: List<Action>) {
        if (!ForegroundService.checkServiceRunning<TapBotAccessibilityService>(context))
            throw AccessibilityServiceNotEnabledException("Accessibility Service is not enabled")

        if (job?.isActive == true) throw TaskAlreadyRunningException("Cancel the previous task first")
        job = runTask(actions)
        _state.value = state.value.copy(running = true, runningTaskId = taskGroupId)
    }

    fun stopTask() {
        job?.cancel()
        job = null
        val oldJobId = _state.value.runningTaskId
        _state.value = state.value.copy(running = false, runningTaskId = null,
            extraInfo = "Old task with id $oldJobId was cancelled")
    }

    private fun Context.sendClick(x: Float, y: Float) {
        val intent = Intent("com.example.tapbot.PERFORM_CLICK").apply {
            putExtra("x", x)
            putExtra("y", y)
        }
        sendBroadcast(intent)
    }

    private fun runTask(actions: List<Action>): Job {
        val job = CoroutineScope(Dispatchers.IO).launch {
            actions.forEach { action ->
                if (action is IndividualTask) {
                    if (action.task is ClickTask) {
                        action.task.delayBeforeTask?.let { delayTime -> delay(delayTime.toLong()) }
                        repeat(action.task.clickCount) { _ ->
                            context.sendClick(action.task.x, action.task.y)
                            delay(action.task.delayBetweenClicks.toLong())
                        }
                    }

                    if (action.task is DelayTask) { delay(action.task.getDelay()) }
                }

                if (action is LoopTask) {
                    var currentLoopStartTime = System.currentTimeMillis()
                    var initializedTime = false
                    var currentIteration = 0
                    var keepRunning = true

                    while (isActive && keepRunning) {
                        currentIteration++

                        action.tasks.forEach { eachAction ->

                            if (eachAction is StartLoop) {
                                if (eachAction.enableTimeCondition) {
                                    if (!initializedTime) currentLoopStartTime = System.currentTimeMillis()
                                    initializedTime = true
                                }
                            }

                            if (eachAction is ClickTask) {
                                eachAction.delayBeforeTask?.let { delayTime -> delay(delayTime.toLong()) }
                                repeat(eachAction.clickCount) { _ ->
                                    context.sendClick(eachAction.x, eachAction.y)
                                    delay(eachAction.delayBetweenClicks.toLong())
                                }
                            }

                            if (eachAction is DelayTask) { delay(eachAction.getDelay()) }

                            if (eachAction is StopLoop) {
                                val stoopLooCondition = StopLoopCondition // initialize the the checker class

                                // get the time condition
                                val timeCondition = eachAction.useOneCondition?.firstConditionOperator?.let {
                                    val elapsedTime = System.currentTimeMillis() - currentLoopStartTime
                                    stoopLooCondition.check(time = eachAction.time, operator = it, elapsedTime = elapsedTime)
                                }

                                // get the count condition
                                val countCondition = eachAction.useOneCondition?.secondConditionOperator?.let {
                                    stoopLooCondition.check(count = currentIteration, operator = it, loopCount = eachAction.count)
                                }

                                // check the time and count condition
                                if (eachAction.enableTimeCondition && eachAction.enableTimeCondition) {
                                    if (timeCondition != null && countCondition != null) {
                                        val comparator = StopLoopCondition.Comparator(timeCondition,
                                            eachAction.useOneCondition.joiners, countCondition)

                                        if (stoopLooCondition.check(comparator)) keepRunning = false
                                    }
                                }

                                // check the time condition
                                if (eachAction.enableTimeCondition) { if (timeCondition != null && timeCondition) keepRunning = false }

                                // check the count condition
                                if (eachAction.enableCountCondition) {
                                    if (countCondition == true) keepRunning = false
                                }
                            }
                        }
                    }
                }
            }
            _state.value = state.value.copy(running = false, runningTaskId = null)
        }
        return job
    }


}
