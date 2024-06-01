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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.ui.screens.settings.widgets.DelayCell
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.theme.delay_action

@Composable
fun DelayActionCell(modifier: Modifier = Modifier, task: DelayTask, onEditTask: (DelayTask) -> Unit,
                    onclickDelete: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 0.4.percentOfScreenHeight())
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(delay_action.copy(alpha = 0.2f))
            .padding(vertical = 1.percentOfScreenHeight())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Delay Action", style = MaterialTheme.typography.bodyMedium,
                maxLines = 2, fontSize = 13.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 1.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            DelayCell(modifier = modifier, label = "Hours", delay = task.delayHour, delayType = "Hr", range =(0..24).toList()) {
                onEditTask(task.copy(delayHour = it))
            }
            DelayCell(modifier = modifier, label = "Minutes", delay = task.delayMinute, delayType = "Min", range = (0..59).toList()
            ) {
                onEditTask(task.copy(delayMinute = it))
            }
            DelayCell(modifier = modifier, label = "Seconds", delay = task.delaySecond, delayType = "Sec", range = (1..100).toList()
            ) {
                onEditTask(task.copy(delaySecond = it))
            }
            IconButton(onClick = { onclickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
        }
    }
}

