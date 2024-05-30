package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.theme.startLoop_action

@Composable
fun StartLoopActionCell(modifier: Modifier = Modifier, task: StartLoop, onEditTask: (StartLoop) -> Unit,
                        onclickDelete: () -> Unit) {
    Column(
        modifier = modifier
            .padding(vertical = 0.4.percentOfScreenHeight())
            .fillMaxWidth()
            .height(10.percentOfScreenHeight())
            .clip(RoundedCornerShape(4.dp))
            .background(startLoop_action.copy(alpha = 0.2f))
            .padding(vertical = 1.percentOfScreenHeight()),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 4.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = "Loop Action", style = MaterialTheme.typography.bodyMedium,
                maxLines = 2, fontSize = 13.sp,
                color = MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.5f),
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 1.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            YesNoCell(label = "Keep Time", delay = task.time, extraInfo = "Ms") {
                onEditTask(task.copy(time = it))
            }
            YesNoCell(label = "Keep Count", delay = task.count, extraInfo = null) {
                onEditTask(task.copy(count = it))
            }

            IconButton(onClick = { onclickDelete() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete icon")
            }
        }
    }
}


@Composable
fun YesNoCell(label: String, delay: Int, extraInfo: String?, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 1.percentOfScreenWidth()),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium,
            maxLines = 2, fontSize = 13.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )

        Row(
            modifier = Modifier
                .border(
                    width = 0.3.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 1.percentOfScreenWidth()),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            CustomYesNoSpinner(selectedItem = delay) {
                onClick(it)
            }

            Spacer(modifier = Modifier.width(1.percentOfScreenWidth()))

            extraInfo?.let {
                Column(
                    verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End
                ) {
                    Text(text = extraInfo, style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2, fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                    )
                }
            }

        }
    }
}

