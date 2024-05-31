package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoopCondition
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.ui.screens.settings.widgets.StopLoopValueCell
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.theme.startLoop_action
import com.example.tapbot.ui.theme.stopLoop_action

@Composable
fun StopLoopActionCell(modifier: Modifier = Modifier, task: StopLoop, startLoop: StartLoop? = null,
                       onEditTask: (StopLoop) -> Unit, onclickDelete: () -> Unit) {

    val expanded = remember { mutableStateOf(false) }
    val useOneCondition = remember { mutableStateOf(task.useOneCondition) }

    Column(
        modifier = modifier
            .padding(top = 0.4.percentOfScreenHeight())
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(stopLoop_action.copy(alpha = 0.2f))
            .padding(top = 1.percentOfScreenHeight()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 4.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Stop Loop Action", style = MaterialTheme.typography.bodyMedium,
                maxLines = 2, fontSize = 13.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 1.percentOfScreenWidth())
                .padding(bottom = 1.percentOfScreenHeight()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            StopLoopValueCell(
                modifier = modifier.width(15.percentOfScreenWidth()),
                label = "Stop if time", delay = task.time, delayType = "Ss",
                items = (0..1000).step(100).toList()) {
                onEditTask(task.copy(time = it))
            }
            StopLoopValueCell(
                modifier = modifier.width(15.percentOfScreenWidth()),
                label = "Stop if count", delay = task.count, delayType = "",
                items = (0..1000).toList()
            ) {
                onEditTask(task.copy(count = it))
            }

            IconButton(onClick = { onclickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
        }


        AnimatedVisibility(visible = expanded.value) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(startLoop_action.copy(alpha = 0.4f))
                    .padding(
                        horizontal = 1.percentOfScreenWidth(),
                        vertical = 1.5.percentOfScreenHeight()
                    ),
                verticalArrangement = Arrangement.spacedBy(1.percentOfScreenHeight())) {

                if (task.useOneCondition == null) {
                    useOneCondition.value = StopLoopCondition.ConditionWithJoiner(
                        firstConditionOperator = StopLoopCondition.Operator.GreaterThan,
                        joiners = StopLoopCondition.Joiners.AND,
                        secondConditionOperator = StopLoopCondition.Operator.GreaterThan
                    )

                    onEditTask(task.copy(useOneCondition = useOneCondition.value))
                }

                Row(
                    Modifier.padding(horizontal = 1.percentOfScreenWidth()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {


                    Text(text = "Set time condition", style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )


                    Spacer(modifier = Modifier.width(10.percentOfScreenWidth()))

                    if (startLoop?.enableTimeCondition == true) {
                        CustomSpinner(
                            modifier = Modifier.width(15.percentOfScreenWidth()),
                            selectedItem = task.time.toString(),
                            items = (1..10000).step(100).toList(),
                            assist = false,
                            onClick = {
                                onEditTask(task.copy(time = it))
                            }
                        )

                        Spacer(modifier = Modifier.width(5.percentOfScreenWidth()))

                        CustomSpinner(
                            selectedItem = task.useOneCondition?.firstConditionOperator?.value ?: "=",
                            values = StopLoopCondition.Operator.values().toList()) {
                            try {
                                onEditTask(task
                                    .copy(useOneCondition = task.useOneCondition
                                        ?.copy(firstConditionOperator = it as StopLoopCondition.Operator)))
                            }catch (_: Exception) { }
                        }

                        Spacer(modifier = Modifier.width(5.percentOfScreenWidth()))

                        Text(text = "keep time", style = MaterialTheme.typography.bodySmall,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        )
                    }


                    if (startLoop?.enableTimeCondition == false) {
                        Text(text = "Enable time condition first", style = MaterialTheme.typography.bodySmall,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        )
                    }
                }



                if (startLoop?.enableCountCondition == true && startLoop.enableTimeCondition) {
                    Spacer(modifier = Modifier.height(1.percentOfScreenHeight()))

                    Row(
                        Modifier.padding(horizontal = 1.percentOfScreenWidth()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(text = "Set gate condition", style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )


                        Spacer(modifier = Modifier.width(10.percentOfScreenWidth()))

                        CustomSpinner(
                            modifier = Modifier.width(15.percentOfScreenWidth()),
                            selectedItem = task.useOneCondition?.joiners?.name ?: "AND",
                            values = StopLoopCondition.Joiners.values().toList()) {
                            try {
                                onEditTask(task
                                    .copy(useOneCondition = task.useOneCondition
                                        ?.copy(joiners = it as StopLoopCondition.Joiners)))
                            }catch (_: Exception) { }
                        }

                        Spacer(modifier = Modifier.width(5.percentOfScreenWidth()))

                        Text(text = "keep count", style = MaterialTheme.typography.bodySmall,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        )

                    }
                }

                Spacer(modifier = Modifier.height(1.percentOfScreenHeight()))

                Row(
                    Modifier.padding(horizontal = 1.percentOfScreenWidth()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(text = "Set count condition", style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2, fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )


                    Spacer(modifier = Modifier.width(10.percentOfScreenWidth()))

                    if (startLoop?.enableCountCondition == true) {
                        CustomSpinner(
                            modifier = Modifier.width(20.percentOfScreenWidth()),
                            selectedItem = task.time.toString(),
                            items = (1..10000).step(100).toList(),
                            assist = false,
                            onClick = {
                                onEditTask(task.copy(count = it))
                            }
                        )

                        Spacer(modifier = Modifier.width(5.percentOfScreenWidth()))

                        CustomSpinner(
                            selectedItem = task.useOneCondition?.secondConditionOperator?.value ?: "=",
                            values = StopLoopCondition.Operator.values().toList()) {
                            try {
                                onEditTask(task
                                    .copy(useOneCondition = task.useOneCondition
                                        ?.copy(secondConditionOperator = it as StopLoopCondition.Operator)))
                            }catch (_: Exception) { }
                        }

                        Spacer(modifier = Modifier.width(5.percentOfScreenWidth()))

                        Text(text = "keep count", style = MaterialTheme.typography.bodySmall,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        )
                    }


                    if (startLoop?.enableCountCondition == false) {
                        Text(text = "Enable count condition first", style = MaterialTheme.typography.bodySmall,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                        )
                    }
                }

            }
        }

        Row(
            modifier = modifier.fillMaxWidth()
                .clickable { expanded.value = !expanded.value },
            horizontalArrangement = Arrangement.Center,
        ) {

            Icon(imageVector = if (expanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Show or hide icon",
                tint = MaterialTheme.colorScheme.inverseOnSurface)
        }
    }
}

