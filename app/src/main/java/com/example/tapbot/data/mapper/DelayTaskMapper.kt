package com.example.tapbot.data.mapper

import com.example.tapbot.data.database.entities.DelayTask

fun DelayTask.toDelayTask(): com.example.tapbot.domain.model.DelayTask {
    return com.example.tapbot.domain.model.DelayTask(
        id = id,
        taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        delayHour = delayHour,
        delayMinute = delayMinute,
        delaySecond = delaySecond
    )
}


fun com.example.tapbot.domain.model.DelayTask.toDelayTask(): DelayTask {
    return DelayTask(
        id = id,
        taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        delayHour = delayHour,
        delayMinute = delayMinute,
        delaySecond = delaySecond
    )
}