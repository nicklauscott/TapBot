package com.example.tapbot.data.mapper

import com.example.tapbot.data.database.entities.ClickTask

fun ClickTask.toClickTask(): com.example.tapbot.domain.model.ClickTask {
    return com.example.tapbot.domain.model.ClickTask(
        id = id,
        taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        delayBeforeTask = delayBeforeTask,
        x = x,
        y = y,
        delayBetweenClicks = delayBetweenClicks,
        clickCount = clickCount

    )
}


fun com.example.tapbot.domain.model.ClickTask.toClickTask(): ClickTask {
    return ClickTask(
        id = id,
        taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        delayBeforeTask = delayBeforeTask,
        x = x,
        y = y,
        delayBetweenClicks = delayBetweenClicks,
        clickCount = clickCount

    )
}