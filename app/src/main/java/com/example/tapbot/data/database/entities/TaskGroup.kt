package com.example.tapbot.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskGroup(
    @PrimaryKey val taskGroupId: String = "",
    val name: String = "",
    val description: String = "",
    val favorite: Boolean = false,
    val iconId: Int = -1
)