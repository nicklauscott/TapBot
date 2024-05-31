package com.example.tapbot.data.mapper

import com.example.tapbot.data.database.entities.TaskGroup

fun TaskGroup.toTaskGroup(): com.example.tapbot.domain.model.TaskGroup {
    return com.example.tapbot.domain.model.TaskGroup(
        taskGroupId = taskGroupId, name = name, description = description,
        favorite = favorite, iconId = iconId
    )
}


fun com.example.tapbot.domain.model.TaskGroup.toTaskGroup(): TaskGroup {
    return TaskGroup(
        taskGroupId = taskGroupId, name = name, description = description,
        favorite = favorite, iconId = iconId
    )
}