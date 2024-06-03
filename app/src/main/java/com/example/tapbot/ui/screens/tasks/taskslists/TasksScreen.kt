package com.example.tapbot.ui.screens.tasks.taskslists

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tapbot.ui.screens.tasks.navigation.TasksScreens
import com.example.tapbot.ui.screens.tasks.taskslists.state_and_events.TasksScreenUiEvent
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.screens.util.rectangularModifier

@Composable
fun TasksScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: TaskScreenViewModel,
    navController: NavController) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val isSearching = rememberSaveable { mutableStateOf(false) }
    val searchText = rememberSaveable { mutableStateOf("") }

    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
            {
                Row(
                    modifier = rectangularModifier(
                        background = MaterialTheme.colorScheme.error,
                        height = 40.dp,
                        width = 100.percentOfScreenWidth(),
                        padding = 2.percentOfScreenWidth()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning Icon")
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = errorMessage.value, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    ) {
        it.calculateTopPadding()
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> TaskScreenLandscape(
                viewModel, searchText = searchText.value, isSearching = isSearching.value,
                onSearchCleared = {
                    isSearching.value = false; searchText.value = ""
                    viewModel.onEvent(TasksScreenUiEvent.ClearSearch)
                },
                onclickSearch = { isSearching.value = true },
                onSearchTextChange = { newText ->
                    searchText.value = newText
                    viewModel.onEvent(TasksScreenUiEvent.SearchTasks(newText))
                },
                onclickFavorite = { viewModel.onEvent(TasksScreenUiEvent.GetFavoriteTasks) },
                onDone = { keyboardController?.hide(); searchText.value = "" },
                onClickCreateTask = {
                    navController.navigate(TasksScreens.TaskDetails.withArg())
                }, onTaskClick = {taskId ->
                    navController.navigate(TasksScreens.TaskDetails.withArg(taskId))}
            )
            else -> TaskScreenPortrait(
                viewModel, searchText = searchText.value, isSearching = isSearching.value,
                onSearchCleared = {
                    isSearching.value = false; searchText.value = ""
                    viewModel.onEvent(TasksScreenUiEvent.ClearSearch)
                },
                onclickSearch = { isSearching.value = true },
                onSearchTextChange = { newText ->
                    searchText.value = newText
                    viewModel.onEvent(TasksScreenUiEvent.SearchTasks(newText))
                },
                onclickFavorite = { viewModel.onEvent(TasksScreenUiEvent.GetFavoriteTasks) },
                onDone = { keyboardController?.hide(); searchText.value = "" },
                onClickCreateTask = {
                    navController.navigate(TasksScreens.TaskDetails.withArg())
                }, onTaskClick = {taskId ->
                    navController.navigate(TasksScreens.TaskDetails.withArg(taskId))})
        }
    }
}