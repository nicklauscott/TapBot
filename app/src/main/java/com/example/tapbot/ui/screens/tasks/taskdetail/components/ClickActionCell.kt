package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.ui.screens.settings.widgets.ClickCell
import com.example.tapbot.ui.screens.settings.widgets.DelayCell
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.theme.click_action
import com.example.tapbot.ui.theme.delay_action

@Composable
fun ClickActionCell(modifier: Modifier = Modifier, task: ClickTask,
                    onclickDelete: () -> Unit, onEditTask: (ClickTask) -> Unit) {

    val expanded = remember { mutableStateOf(false) }

    Column(
            modifier = modifier
                .padding(vertical = 0.4.percentOfScreenHeight())
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(click_action.copy(alpha = 0.2f))
                .padding(vertical = 1.percentOfScreenHeight())
    ) {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.percentOfScreenWidth(),
                        vertical = 0.6.percentOfScreenHeight()
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = "Click Action", style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2, fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
                )
            }

            Row(modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 1.percentOfScreenWidth()),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                ScreenCoordinates(
                    modifier = Modifier.width(20.percentOfScreenWidth()),
                    task = task, range = (0..2000).step(20).toList(),
                    onChangeX = { onEditTask(task.copy(x = it.toFloat())) },
                    onChangeY = { onEditTask(task.copy(y = it.toFloat())) },
                    assist = false)

                ClickCell(label = "Clicks", task = task, range = (1..1000).toList()) {
                    onEditTask(task.copy(clickCount = it))
                }

                IconButton(onClick = { onclickDelete() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
                }
            }


            AnimatedVisibility(visible = expanded.value) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(delay_action.copy(alpha = 0.2f))
                        .padding(
                            horizontal = 1.percentOfScreenWidth(),
                            vertical = 1.percentOfScreenHeight()
                        ),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Start Delay", style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                        Spacer(modifier = Modifier.width(2.percentOfScreenWidth()))
                        DelayCell(label = "", delay = task.delayBeforeTask ?: -1,  delayType = "Ss", range = (0..100).toList()) {
                            onEditTask(task.copy(delayBeforeTask = it))
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Clicks Delay", style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2, fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                        Spacer(modifier = Modifier.width(2.percentOfScreenWidth()))

                        DelayCell(label = "", delay = task.delayBetweenClicks, delayType = "Ms", range = (0..10000).step(5).toList()) {
                            onEditTask(task.copy(delayBetweenClicks = it))
                        }
                    }
                }
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { expanded.value = !expanded.value },
                horizontalArrangement = Arrangement.Center,
            ) {

                Icon(
                    imageVector = if (expanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Show or hide icon",
                    tint = MaterialTheme.colorScheme.inverseOnSurface
                )
            }

        }

}


@Composable
fun ClickActionCellLandscape(modifier: Modifier = Modifier, cellModifier: Modifier = Modifier, task: ClickTask,
                    onclickDelete: () -> Unit, onEditTask: (ClickTask) -> Unit) {
    Column(
        modifier = cellModifier
            .padding(vertical = 1.percentOfScreenHeight())
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(click_action.copy(alpha = 0.2f))
            .padding(vertical = 2.percentOfScreenHeight())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.percentOfScreenWidth(),
                    vertical = 1.percentOfScreenHeight()
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Click Action", style = MaterialTheme.typography.bodyMedium,
                maxLines = 2, fontSize = 13.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
            )
        }

        Row(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 1.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            ScreenCoordinatesLandscape(
                modifier = modifier,
                task = task, range = (0..2000).step(20).toList(),
                onChangeX = { onEditTask(task.copy(x = it.toFloat())) },
                onChangeY = { onEditTask(task.copy(y = it.toFloat())) },
                assist = false)

            ClickCell(modifier = modifier, label = "Clicks", task = task, range = (1..10000).toList()) {
                onEditTask(task.copy(clickCount = it))
            }

            DelayCell(modifier = modifier, label = "Start Delay", delay = task.delayBeforeTask ?: -1,  delayType = "Ss", range = (0..100).toList()) {
                onEditTask(task.copy(delayBeforeTask = it))
            }

            DelayCell(modifier = modifier, label = "Clicks Delay", delay = task.delayBetweenClicks, delayType = "Ms", range = (0..10000).step(5).toList()) {
                onEditTask(task.copy(delayBetweenClicks = it))
            }

            IconButton(onClick = { onclickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
        }
    }
}

