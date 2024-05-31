package com.example.tapbot.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ClickTask(
    @PrimaryKey val id: String = "",
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,
    val delayBeforeTask: Int? = 3,
    val x: Float = 50f,
    val y: Float = 70f,
    val delayBetweenClicks: Int = 1,
    val clickCount: Int = 1
)