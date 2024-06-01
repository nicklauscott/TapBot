package com.example.tapbot.ui.screens.tasks.taskdetail.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun TasksDetailTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    canSave: Boolean = false,
    canPlay: Boolean = false,
    favorite: Boolean = false,
    onClickSave: () -> Unit = { },
    onClickDelete: () -> Unit = { },
    onClickPlay: () -> Unit = { },
    onToggleFavorite: () -> Unit = { },
    onClickBack: () -> Unit = { },
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onBackground,
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(8.percentOfScreenHeight()),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp,
            shape = RectangleShape,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 1.percentOfScreenWidth()),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {

                    IconButton(onClick = onClickBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Icon",
                            modifier = Modifier.size(25.dp)
                        )
                    }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground)
                }


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {

                    if (!canSave && canPlay) {
                        IconButton(onClick = onToggleFavorite) {
                            Icon(
                                imageVector = if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }

                    if (!canSave && canPlay) {
                        IconButton(onClick = onClickPlay) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }

                    if (canSave) {
                        IconButton(onClick = onClickSave) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }

                    if (!canSave && canPlay) {
                        IconButton(onClick = onClickDelete) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Back Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}