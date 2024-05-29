package com.example.tapbot.ui.screens.tasks.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tapbot.ui.screens.tasks.taskdetail.TaskDetailScreen
import com.example.tapbot.ui.screens.tasks.taskdetail.TaskDetailViewModel
import com.example.tapbot.ui.screens.tasks.taskslists.TaskScreenViewModel
import com.example.tapbot.ui.screens.tasks.taskslists.TasksScreen

@Composable
fun TasksNavigation(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = TasksScreens.TasksList.name) {

        composable(TasksScreens.TasksList.name) {
            val viewModel: TaskScreenViewModel = hiltViewModel()
            TasksScreen(windowSizeClass = windowSizeClass,
                viewModel = viewModel,
                navController = navController)
        }

        composable(TasksScreens.TaskDetails.name + "/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    defaultValue = "-1"
                    nullable = true
                }
            )
        ) {
            val viewModel: TaskDetailViewModel = hiltViewModel()
            TaskDetailScreen(windowSizeClass = windowSizeClass,
                navController = navController, viewModel = viewModel)
        }
    }
}