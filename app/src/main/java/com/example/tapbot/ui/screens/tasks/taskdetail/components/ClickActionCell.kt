package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.ui.screens.settings.widgets.ClickCell
import com.example.tapbot.ui.screens.settings.widgets.DelayCell
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.theme.click_action

@Composable
fun ClickActionCell(modifier: Modifier = Modifier, task: ClickTask,
                    onclickDelete: () -> Unit, onEditTask: (ClickTask) -> Unit) {
    Column(
        modifier = modifier
            .padding(vertical = 0.4.percentOfScreenHeight())
            .fillMaxWidth()
            .height(10.percentOfScreenHeight())
            .clip(RoundedCornerShape(4.dp))
            .background(click_action.copy(alpha = 0.2f)),
    ) {
        Row(modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 1.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            ScreenCoordinates(
                task = task, range = 0..100,
                onChangeX = { onEditTask(task.copy(x = it.toFloat())) },
                onChangeY = { onEditTask(task.copy(y = it.toFloat())) },
                assist = false)

            ClickCell(label = "Clicks", task = task, range = 1..100) {
                onEditTask(task.copy(clickCount = it))
            }

            DelayCell(label = "Start Delay", delay = task.delayBeforeTask ?: -1,  delayType = "Ss", range = 1..20) {
                onEditTask(task.copy(delayBeforeTask = it))
            }

            DelayCell(label = "Clicks Delay", delay = task.delayBetweenClicks, delayType = "Ms", range = 1..20) {
                onEditTask(task.copy(delayBetweenClicks = it))
            }

            IconButton(onClick = { onclickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
        }
    }
}

