package com.example.tapbot.domain.model

import java.util.UUID

enum class Actions(val value: String) {
    CLICK(value = "Click"),
    DELAY(value = "Delay"),
    LOOP(value = "Loop"),
    STOP_LOOP(value = "Stop Loop")
}

sealed class Task

data class TaskGroup(
    val tasGroupId: String = "",
    val name: String = "",
    val description: String = "",
    val iconId: Int = -1
)


data class ClickTask(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val standaloneTask: Boolean = true,
    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,

    val delayBeforeTask: Int? = 3,
    val x: Float = 50f,
    val y: Float = 70f,
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

    val delayHour: Int = 0,
    val delayMinute: Int = 0,
    val delaySecond: Int = 1,
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

    val time: Int = 0,
    val count: Int = 0,
    val secondCount: Int = 0,

    val childrenTaskIds: List<String> = emptyList()
): Task() {
    val enableTimeCondition: Boolean
        get() = time > 0

    val enableCountCondition: Boolean
        get() = count > 0

    val enableSecondCountCondition: Boolean
        get() = secondCount > 0
}

data class StopLoop(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val parentTaskId: String? = null,
    val childIdIndexId: Int? = null,

    val time: Int = 0,
    val count: Int = 0,
    val secondCount: Int = 0,
): Task() {
    val enableTimeCondition: Boolean
        get() = time > 0

    val enableCountCondition: Boolean
        get() = count > 0

    val enableSecondCountCondition: Boolean
        get() = secondCount > 0
}

