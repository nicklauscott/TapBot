package com.example.tapbot.ui.screens.tasks.taskdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun TaskDetailScreen(windowSizeClass: WindowSizeClass,
                     navController: NavController,
                     viewModel: TaskDetailViewModel) {

    val snackBarHostState = remember { SnackbarHostState() }

    Column(modifier = Modifier.fillMaxSize()) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Expanded -> TasksDetailLandscape(
                navController = navController, viewModel = viewModel,
                snackBarHostState = snackBarHostState
            )
            else -> TasksDetailPortrait(
                navController = navController, viewModel = viewModel, snackBarHostState = snackBarHostState
            )
        }
    }
}
