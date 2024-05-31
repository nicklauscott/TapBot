package com.example.tapbot.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DelayTask(
    @PrimaryKey val id: String = "",
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,
    val delayHour: Int = 0,
    val delayMinute: Int = 0,
    val delaySecond: Int = 1,
)