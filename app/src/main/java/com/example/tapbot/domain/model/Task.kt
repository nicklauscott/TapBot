package com.example.tapbot.domain.model

import java.util.UUID

enum class Actions(val value: String) {
    CLICK(value = "Click"),
    DELAY(value = "Delay"),
    LOOP(value = "Loop"),
    STOP_LOOP(value = "Stop Loop")
}

sealed class Task

data class TaskGroupList(
    val taskGroup: TaskGroup? = null,
    val tasks: List<Task> = emptyList()
)

data class TaskGroup(
    val taskGroupId: String = "",
    val name: String = "",
    val description: String = "",
    val favorite: Boolean = false,
    val iconId: Int = -1
)

data class ClickTask(
    val id: String = UUID.randomUUID().toString(),
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val delayBeforeTask: Int? = 3,
    val x: Float = 50f,
    val y: Float = 70f,
    val delayBetweenClicks: Int = 1,
    val clickCount: Int = 1
) : Task()

data class DelayTask(
    val id: String = UUID.randomUUID().toString(),
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val delayHour: Int = 0,
    val delayMinute: Int = 0,
    val delaySecond: Int = 1,
) : Task()  {

    fun getDelay(): Long {
        return (delayHour * 3600 + delayMinute * 60 + delaySecond) * 1000L
    }

}

data class StartLoop(
    val id: String = UUID.randomUUID().toString(),
    val taskGroupId: String = "",
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
    val taskGroupId: String = "",
    val taskNumberInTheList: Int = 0,

    val prentLoopId: String? = null,
    val time: Int = 0,
    val count: Int = 0,
    val optionalVariable: Int = 0,

    // advanced
    val useOneCondition: StopLoopCondition.ConditionWithJoiner? = null,

    ) : Task() {

        val enableTimeCondition: Boolean
        get() = time != 0

        val enableCountCondition: Boolean
        get() = count != 0
}



object StopLoopCondition {

    enum class Operator(val value: String) {
        Equals(value = "="),
        NotEquals(value = "!="),
        LessThan(value = "<"),
        LessThanEquals("<="),
        GreaterThan(value = ">"),
        GreaterThanEquals(value = "=>")
    }


    enum class Joiners {
        AND,
        OR
    }

    data class ConditionWithJoiner(
        val firstConditionOperator: Operator,
        val joiners: Joiners,
        val secondConditionOperator: Operator
    )

    data class Comparator(
        val first: Boolean,
        val joiners: Joiners,
        val second: Boolean,
    )

    fun check(comparator: Comparator): Boolean{
        return when (comparator.joiners) {
            Joiners.AND -> comparator.first && comparator.second
            Joiners.OR -> comparator.first || comparator.second
        }
    }

    data class LongChecker(
        val firstType: Long,
        val condition: Operator,
        val secondType: Long,
    )

    data class IntChecker(
        val firstType: Int,
        val condition: Operator,
        val secondType: Int,
    )

    private fun runConditionLong(condition: LongChecker): Boolean{
        return when (condition.condition) {
            Operator.Equals -> {
                condition.firstType == condition.secondType // getType(condition.secondType)
            }
            Operator.NotEquals -> {
                condition.firstType != condition.secondType
            }
            Operator.LessThan -> {
                condition.firstType < condition.secondType
            }
            Operator.LessThanEquals -> {

                condition.firstType <= condition.secondType
            }
            Operator.GreaterThan -> {

                condition.firstType > condition.secondType
            }
            Operator.GreaterThanEquals -> {

                condition.firstType >= condition.secondType
            }
        }
    }

    fun check(time: Int, operator: Operator, elapsedTime: Long): Boolean{
        val checker = LongChecker(firstType = time.toLong(), condition = operator, secondType = elapsedTime / 1000L)
        return runConditionLong(checker)
    }

    private fun runConditionInt(condition: IntChecker): Boolean{
        return when (condition.condition) {
            Operator.Equals -> {
                condition.firstType == condition.secondType // getType(condition.secondType)
            }
            Operator.NotEquals -> {
                condition.firstType != condition.secondType
            }
            Operator.LessThan -> {
                condition.firstType < condition.secondType
            }
            Operator.LessThanEquals -> {

                condition.firstType <= condition.secondType
            }
            Operator.GreaterThan -> {

                condition.firstType > condition.secondType
            }
            Operator.GreaterThanEquals -> {

                condition.firstType >= condition.secondType
            }
        }
    }

    fun check(count: Int, operator: Operator, loopCount: Int): Boolean{
        val checker = IntChecker(firstType = count, condition = operator, secondType = loopCount)
        return runConditionInt(checker)
    }

}

