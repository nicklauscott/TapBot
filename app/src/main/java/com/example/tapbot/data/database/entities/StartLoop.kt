package com.example.tapbot.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class StartLoop(
    @PrimaryKey val id: String = "",
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,
    val time: Int = 0,
    val count: Int = 1,
)