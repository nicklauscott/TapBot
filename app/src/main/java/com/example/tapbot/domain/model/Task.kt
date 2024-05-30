package com.example.tapbot.domain.model

import java.util.UUID

enum class Actions(val value: String) {
    CLICK(value = "Click"),
    DELAY(value = "Delay"),
    LOOP(value = "Loop"),
    STOP_LOOP(value = "Stop Loop")
}

sealed class Task

data class TaskGroup(
    val tasGroupId: String = "",
    val name: String = "",
    val description: String = "",
    val iconId: Int = -1
)

data class ClickTask(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val delayBeforeTask: Int? = 3,
    val x: Float = 50f,
    val y: Float = 70f,
    val delayBetweenClicks: Int = 1,
    val clickCount: Int = 1
) : Task()

data class DelayTask(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val delayHour: Int = 0,
    val delayMinute: Int = 0,
    val delaySecond: Int = 1,
) : Task()

data class StartLoop(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val time: Int = 0,
    val count: Int = 1,
) : Task() {
    val enableTimeCondition: Boolean
        get() = time == 1

    val enableCountCondition: Boolean
        get() = count == 1

}

data class StopLoop(
    val id: String = UUID.randomUUID().toString(),
    val tasGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val prentLoopId: String? = null,
    val time: Int = 0,
    val count: Int = 0,
    val optionalVariable: Int = 0,

    // advanced
    val useOneCondition: ConditionWithJoiner? = null,

    ) : Task() {

    enum class Operator(val value: String) {
        Equals(value = "="),
        NotEquals(value = "!="),
        LessThan(value = "<"),
        LessThanEquals("<="),
        GreaterThan(value = ">"),
        GreaterThanEquals(value = "=>")
    }

    enum class Types {
        Time,
        Count,
    }


    data class Condition(
        val firstType: Types,
        val condition: Operator,
        val secondType: Types,
    )

    private fun getType(type: Types): Int {
        return when (type) {
            Types.Time -> time
            Types.Count -> count
        }
    }

    enum class Joiners {
        AND,
        OR
    }

    data class ConditionWithJoiner(
        val firstCondition: Condition,
        val joiners: Joiners,
        val secondCondition: Condition
    )

    private fun runCondition(condition: Condition): Boolean{
        return when (condition.condition) {
            Operator.Equals -> {
                 (getType(condition.firstType) == getType(condition.secondType))
            }
            Operator.NotEquals -> {
                (getType(condition.firstType) != getType(condition.secondType))
            }
            Operator.LessThan -> {
                (getType(condition.firstType) < getType(condition.secondType))
            }
            Operator.LessThanEquals -> {

                (getType(condition.firstType) <= getType(condition.secondType))
            }
            Operator.GreaterThan -> {

                (getType(condition.firstType) > getType(condition.secondType))
            }
            Operator.GreaterThanEquals -> {

                (getType(condition.firstType) >= getType(condition.secondType))
            }
        }
    }

    fun ConditionWithJoiner.check(): Boolean{
        return when (joiners) {
            Joiners.AND -> runCondition(firstCondition)
                    && runCondition(secondCondition)
            Joiners.OR -> runCondition(firstCondition)
                    || runCondition(secondCondition)
        }
    }
}

