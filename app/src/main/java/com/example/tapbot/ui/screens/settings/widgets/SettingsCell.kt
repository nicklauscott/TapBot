package com.example.tapbot.ui.screens.settings.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

data class SettingItem(
    @DrawableRes val icon: Int,
    val title: String,
    val description: String
)

@Composable
fun SettingCell(modifier: Modifier = Modifier,
                settingItem: SettingItem, onclick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(10.percentOfScreenHeight())
            .clickable { onclick() }
            .padding(start = 5.percentOfScreenWidth(), end = 2.percentOfScreenWidth(),
                top = 2.percentOfScreenHeight(), bottom = 2.percentOfScreenHeight()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Image(painter = painterResource(id = settingItem.icon),
            contentDescription = settingItem.description,
            modifier = Modifier.height(20.percentOfScreenHeight()),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.8f)))



        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = settingItem.title, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
            )

            Text(text = settingItem.description, style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(80.percentOfScreenWidth()), maxLines = 2,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f),
            )
        }
    }
}

