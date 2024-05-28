package com.example.tapbot.ui.screens.settings.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tapbot.ui.screens.util.cornerRadius
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun EnableMainFeature(modifier: Modifier = Modifier, innerModifier: Modifier = Modifier,
                      title: String = "Enable Tap Bot",
                      isEnabled: Boolean, onclick: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(8.5.percentOfScreenHeight()),
        color = if (isEnabled) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(corner = CornerSize(cornerRadius())),
        onClick = { onclick() }) {
        Row(
            modifier = innerModifier
                .fillMaxSize()
                .padding(horizontal = 5.percentOfScreenWidth()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
            )

            Switch(checked = isEnabled, onCheckedChange = { onclick() },
                colors = SwitchDefaults.colors(
                    uncheckedBorderColor = MaterialTheme.colorScheme.surfaceTint,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    uncheckedTrackColor = MaterialTheme.colorScheme.inversePrimary,

                    checkedThumbColor = MaterialTheme.colorScheme.primaryContainer,
                ))
        }
    }
}