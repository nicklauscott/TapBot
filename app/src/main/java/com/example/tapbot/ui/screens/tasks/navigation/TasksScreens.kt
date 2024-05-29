package com.example.tapbot.ui.screens.tasks.navigation

enum class TasksScreens {
    TasksList,
    TaskDetails;

    fun withArg(vararg args: String): String {
        return buildString {
            append(name)
            if (args.isEmpty()) { append("/-1") }
            args.forEach { append("/$it") }
        }
    }
}