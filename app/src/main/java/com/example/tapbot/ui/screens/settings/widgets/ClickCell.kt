package com.example.tapbot.ui.screens.settings.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.ui.screens.tasks.taskdetail.components.CustomSpinner
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun ClickCell(modifier: Modifier = Modifier, label: String, task: ClickTask, range: List<Int>, onClick: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 1.percentOfScreenWidth()),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Clicks", style = MaterialTheme.typography.bodyMedium,
            maxLines = 2, fontSize = 13.sp,
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )

        Row(
            modifier = Modifier
                .border(width = 0.3.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 1.percentOfScreenWidth()),
            horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ) {
            CustomSpinner(modifier = modifier, selectedItem = task.clickCount.toString(), items = range, assist = false) {
                onClick(it)
            }
        }
    }
}