package com.example.tapbot.ui.screens.tasks.taskslists.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tapbot.R
import com.example.tapbot.domain.model.TaskGroup
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun TasksGroupCell(modifier: Modifier = Modifier, taskGroup: TaskGroup, onClick: (String) -> Unit = {}) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(15.percentOfScreenHeight())
            .padding(vertical = 1.percentOfScreenHeight()),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = { onClick(taskGroup.tasGroupId) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = 1.percentOfScreenHeight(),
                    horizontal = 2.percentOfScreenWidth()
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            val resourceId = if (taskGroup.iconId == -1) R.drawable.tap_logo else taskGroup.iconId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.percentOfScreenHeight()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = resourceId),
                    contentDescription = "${taskGroup.name} icon",
                    modifier = Modifier.height(20.percentOfScreenHeight()),
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inverseOnSurface))

                Spacer(modifier = Modifier.width(2.percentOfScreenWidth()))

                Text(
                    text = taskGroup.name.take(15), style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                )
            }

            Spacer(modifier = Modifier.height(1.percentOfScreenHeight()))

            Text(text = taskGroup.description, style = MaterialTheme.typography.labelSmall,
                maxLines = 2,
                color = MaterialTheme.colorScheme.inverseOnSurface,
            )

        }
    }
}