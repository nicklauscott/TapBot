package com.example.tapbot.domain.model

interface Action

data class IndividualTask(val task: Task): Action

data class LoopTask(val tasks: List<Task>): Action