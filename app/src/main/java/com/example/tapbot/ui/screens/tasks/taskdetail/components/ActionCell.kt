package com.example.tapbot.ui.screens.tasks.taskdetail.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun ActionCell(modifier: Modifier = Modifier, resourceId: Int, action: String, oncClick: () -> Unit) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { oncClick() }
        .padding(vertical = 2.percentOfScreenHeight(), horizontal = 3.percentOfScreenWidth()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {

        Image(painter = painterResource(id = resourceId),
            contentDescription = "$action icon",
            modifier = Modifier.size(3.5.percentOfScreenHeight()),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primary))

        Spacer(modifier = Modifier.width(5.percentOfScreenHeight()))

        Text(
            text = action, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
