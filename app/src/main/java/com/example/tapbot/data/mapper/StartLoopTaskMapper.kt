package com.example.tapbot.data.mapper

import com.example.tapbot.data.database.entities.StartLoop

fun StartLoop.toStartLoop(): com.example.tapbot.domain.model.StartLoop {
    return com.example.tapbot.domain.model.StartLoop(
        id = id, taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        time = time, count = count
    )
}


fun com.example.tapbot.domain.model.StartLoop.toStartLoop(): StartLoop {
    return StartLoop(
        id = id, taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        time = time, count = count
    )
}