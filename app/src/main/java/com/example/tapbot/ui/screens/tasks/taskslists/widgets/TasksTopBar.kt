package com.example.tapbot.ui.screens.tasks.taskslists.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun TasksTopBar(
    modifier: Modifier = Modifier,
    title: String = "Tasks",
    isSearch: Boolean = true,
    searchText: String,
    onclickSearch: () -> Unit = { },
    showingFavorite: Boolean = false,
    onclickFavorite: () -> Unit = { },
    onSearchTextChange: (String) -> Unit = { },
    onDone: () -> Unit = { },
    onCancelSearch: () -> Unit = { }
) {

    Row {

        // normal use
        Surface(
            modifier = modifier
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = LinearEasing
                    )
                )
                .weight(if (!isSearch) 1f else 0.01f)
                .height(8.percentOfScreenHeight()),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp,
            shape = RectangleShape,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.percentOfScreenWidth()),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium, fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.onBackground)


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onclickFavorite) {
                        Icon(
                            imageVector = if (!showingFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    IconButton(onClick = onclickSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                }

            }
        }

        // search use
        Surface(
            modifier = modifier
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 350,
                        easing = LinearEasing
                    )
                )
                .weight(if (isSearch) 1f else 0.01f)
                .height(8.percentOfScreenHeight()),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp,
            shape = RectangleShape,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.percentOfScreenWidth()),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    // text field
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                        Column(
                            modifier = Modifier
                                .width(80.percentOfScreenWidth())
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(text = if (searchText == "") "    Search..." else "",
                                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f))
                        }
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            value = searchText,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.background
                            ),
                            onValueChange = {
                                onSearchTextChange(it)
                            },
                            maxLines = 1,
                            trailingIcon = {
                                IconButton(onClick = onCancelSearch ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Cancel Icon",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { onDone() })
                        )
                    }

                }
            }
        }

    }


}