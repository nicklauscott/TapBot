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
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.ui.screens.settings.widgets.DelayCell
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.theme.stopLoop_action

@Composable
fun StopLoopActionCell(modifier: Modifier = Modifier, task: StopLoop, onEditTask: (StopLoop) -> Unit,
                       onclickDelete: () -> Unit) {
    Column(
        modifier = modifier
            .padding(vertical = 0.4.percentOfScreenHeight())
            .fillMaxWidth()
            .height(10.percentOfScreenHeight())
            .clip(RoundedCornerShape(4.dp))
            .background(stopLoop_action.copy(alpha = 0.2f)),
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 1.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            DelayCell(label = "Stop if hours", delay = task.time, delayType = "Ss", range = 0..24) {
                onEditTask(task.copy(time = it))
            }
            DelayCell(label = "Stop if first count", delay = task.count, delayType = "", range = 0..59
            ) {
                onEditTask(task.copy(count = it))
            }
            DelayCell(label = "Stop if second count", delay = task.secondCount, delayType = "", range = 0..59
            ) {
                onEditTask(task.copy(secondCount = it))
            }
            IconButton(onClick = { onclickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
        }
    }
}

