package com.example.tapbot.ui.screens.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.tapbot.ui.screens.tasks.widgets.TasksGroupCell
import com.example.tapbot.ui.screens.tasks.widgets.TasksTopBar
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun TaskScreenLandscape(
    viewModel: TaskScreenViewModel, searchText: String, isSearching: Boolean,
    onSearchCleared: () -> Unit, onclickSearch: () -> Unit = { },
    onDone: () -> Unit = { }, onSearchTextChange: (String) -> Unit,
    onClickCreateTask: () -> Unit, onTaskClick: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                TasksTopBar(modifier = Modifier.height(14.percentOfScreenHeight()),
                    isSearch = isSearching, onclickSearch = onclickSearch, onDone = onDone,
                    searchText = searchText, onCancelSearch = onSearchCleared, onSearchTextChange = onSearchTextChange)
            }
        },
        floatingActionButton = {
            Card(
                modifier = Modifier
                    .size(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint
                ),
                shape = CircleShape,
                onClick = { onClickCreateTask() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            vertical = 1.percentOfScreenHeight(),
                            horizontal = 1.percentOfScreenWidth()
                        ),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add icon",
                        modifier = Modifier.size(35.dp),
                        tint = MaterialTheme.colorScheme.onSurface)
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        when  {
            viewModel.state.value.loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(it)) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 2.percentOfScreenHeight()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(modifier = Modifier.height(16.percentOfScreenHeight()))
                    }

                    items(viewModel.state.value.tasks) { taskGroup ->
                        TasksGroupCell(modifier = Modifier.height(10.percentOfScreenWidth())
                            .padding(horizontal = 2.percentOfScreenWidth(), vertical = 0.3.percentOfScreenWidth()),
                            taskGroup) { task ->
                            onTaskClick(task)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(5.percentOfScreenHeight()))
                    }
                }
            }
        }
    }
}