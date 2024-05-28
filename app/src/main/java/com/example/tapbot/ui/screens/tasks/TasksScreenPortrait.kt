package com.example.tapbot.ui.screens.tasks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tapbot.ui.screens.tasks.widgets.TasksGroupCell
import com.example.tapbot.ui.screens.tasks.widgets.TasksTopBar
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun TaskScreenPortrait(
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
                TasksTopBar(isSearch = isSearching, onclickSearch = onclickSearch, onDone = onDone,
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
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
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
                        Spacer(modifier = Modifier.height(9.percentOfScreenHeight()))
                    }

                    items(viewModel.state.value.tasks) { taskGroup ->
                        TasksGroupCell(modifier = Modifier, taskGroup) { task ->
                            onTaskClick(task)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(2.percentOfScreenHeight()))
                    }
                }
            }
        }
    }
}


