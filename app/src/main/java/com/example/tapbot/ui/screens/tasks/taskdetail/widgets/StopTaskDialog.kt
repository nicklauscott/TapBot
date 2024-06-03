package com.example.tapbot.ui.screens.tasks.taskdetail.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.ui.screens.util.cornerRadius
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopTaskDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit, onConfirm: () -> Unit) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(cornerRadius()))
            .padding(vertical = 4.percentOfScreenHeight(), horizontal = 4.percentOfScreenWidth()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Stop current task and start a new one?", style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(5.percentOfScreenHeight()))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {

                Button(
                    modifier = modifier
                        .width(25.percentOfScreenWidth())
                        .height(5.percentOfScreenHeight()),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.95f),
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    ),
                    onClick = onDismissRequest ) {
                    Text(text = "Cancel", style = MaterialTheme.typography.bodySmall, fontSize = 15.sp)
                }

                Button(
                    modifier = modifier
                        .width(25.percentOfScreenWidth())
                        .height(5.percentOfScreenHeight()),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.background,
                        disabledContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                        disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    ),
                    onClick = onConfirm ) {
                    Text(text = "Yes", color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall, fontSize = 15.sp)
                }
            }

        }
    }
}