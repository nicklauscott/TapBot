package com.example.tapbot.ui.screens.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import kotlinx.coroutines.delay

@Composable
fun InfoScreen(modifier: Modifier = Modifier) {
    //Test()

    val loop1Time = remember { mutableIntStateOf(0) }
    val loop1Count = remember { mutableIntStateOf(0) }

    val loop2Time = remember { mutableIntStateOf(0) }
    val loop2Count = remember { mutableIntStateOf(0) }


    val loop3Time = remember { mutableIntStateOf(0) }
    val loop3Count = remember { mutableIntStateOf(0) }

    val currentTask = remember { mutableStateOf(listOf("Delaying", "Clicking")) }

    LaunchedEffect(true) {
        while (true) {
            delay((1..100).random().toLong())
            if (loop1Time.intValue < 10000) loop1Time.intValue += (20..100).random()
            if (loop1Count.intValue < 9581) loop1Count.intValue += (20..100).random()
            if (loop1Time.intValue >= 10000 && loop1Count.intValue >= 9581) {

                if (loop2Time.intValue <= 50000) loop2Time.intValue += (200..1000).random()
                if (loop2Count.intValue <= 60000) loop2Count.intValue += (200..1000).random()

                if (loop2Time.intValue >= 50000 && loop2Count.intValue >= 90000) {
                    if (loop3Time.intValue <= 500) loop3Time.intValue += (1..100).random()
                    if (loop3Count.intValue <= 2500) loop3Count.intValue += (1..100).random()
                    if (loop3Time.intValue >= 500 && loop3Count.intValue >= 2500) {
                        break
                    }
                }
            }
        }
    }

    OverlayTaskDetail(
        loopsDetails = arrayOf(
            listOf(
                listOf(loop1Time.intValue.toString(), "10000"),
                listOf(loop1Count.intValue.toString(), "9581")
            ),
            listOf(
                listOf(loop2Time.intValue.toString(), "50000"),
                listOf(loop2Count.intValue.toString(), "60000")
            ),
            listOf(
                listOf(loop3Time.intValue.toString(), "500"),
                listOf(loop3Count.intValue.toString(), "2500")
            )
        ),
        currentTask = currentTask.value.random()
    )
}

@Composable
fun OverlayTaskDetail(
    // loop 1 = listOf [ [elapsedTime / TotalTime], [count / TotalCount] ]
    vararg loopsDetails: List<List<String>> = arrayOf(
        listOf(listOf("10000", "50000"), listOf("40000", "40000")),
        listOf(listOf("0", "10"), listOf("0", "5")),
        listOf(listOf("0", "8"), listOf("0", "5")),
    ),
    currentTask: String = "Delaying",
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.percentOfScreenHeight())
            .padding(vertical = 5.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.End
    ) {

        Column(
            modifier = Modifier.width(50.percentOfScreenWidth()),
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
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )
            }
            loopsDetails.forEachIndexed { index, it ->
                Column(modifier = Modifier.padding(vertical = 5.dp)) {
                    Text(
                        text = "Loop ${index + 1}",
                        modifier = Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
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
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = value[0],
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = value[1],
                                modifier = Modifier.weight(0.25f),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
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
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )

                Text(
                    text = currentTask,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f),
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }

    }
}