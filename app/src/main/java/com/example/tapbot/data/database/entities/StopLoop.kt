package com.example.tapbot.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StopLoop(
    @PrimaryKey val id: String = "",
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,
    val prentLoopId: String? = null,
    val time: Int = 0,
    val count: Int = 0,
    val optionalVariable: Int = 0,
    val useOneCondition: String? = null
)