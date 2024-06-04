package com.example.tapbot.data.sevices.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun OverlayTaskDetail(
    vararg loopsDetails: List<List<String>> = arrayOf(
        listOf(listOf("10000", "50000"), listOf("40000", "40000")),
        listOf(listOf("0", "10"), listOf("0", "5")),
        listOf(listOf("0", "8"), listOf("0", "5")),
    ),
    currentTask: String = "Delaying",
) {
    Column(
        modifier = Modifier
            .width(50.percentOfScreenWidth())
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top
    ) {

        Column(
            modifier = Modifier
                .width(50.percentOfScreenWidth())
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.95f))
                .padding(start = 5.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(end = 5.percentOfScreenWidth())
                    .align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Elapsed",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
            }
            loopsDetails.forEachIndexed { index, it ->
                Column(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        text = "Loop ${index + 1}",
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                    )
                    it.forEachIndexed { valueIndex, value ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (valueIndex == 0) "Time" else "Count",
                                modifier = Modifier.weight(0.5f),
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = value[0],
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.55f),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = value[1],
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colorScheme.background.copy(alpha = 0.55f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .padding(end = 5.percentOfScreenWidth()),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current Task:",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                )

                Text(
                    text = currentTask,
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }

    }
}