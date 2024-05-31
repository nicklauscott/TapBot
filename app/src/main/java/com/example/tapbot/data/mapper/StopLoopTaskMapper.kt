package com.example.tapbot.data.mapper

import com.example.tapbot.data.database.entities.StopLoop
import com.example.tapbot.domain.model.StopLoopCondition
import com.google.gson.Gson

fun StopLoop.toStopLoop(): com.example.tapbot.domain.model.StopLoop {
    val gson = Gson()
    return com.example.tapbot.domain.model.StopLoop(
        id = id,
        taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        prentLoopId = prentLoopId,
        time = time,
        count = count,
        optionalVariable = optionalVariable,
        useOneCondition = gson.fromJson(useOneCondition, StopLoopCondition.ConditionWithJoiner::class.java)
    )
}



fun com.example.tapbot.domain.model.StopLoop.toStopLoop(): StopLoop {
    val gson = Gson()
    return StopLoop(
        id = id,
        taskGroupId = taskGroupId,
        taskNumberInTheList = taskNumberInTheList,
        prentLoopId = prentLoopId,
        time = time,
        count = count,
        optionalVariable = optionalVariable,
        useOneCondition = gson.toJson(useOneCondition)
    )
}