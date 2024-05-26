package com.example.tapbot.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tapbot.ui.screens.home.HomeScreen
import com.example.tapbot.ui.screens.home.HomeScreenViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Home.name) {

        composable(route = Screens.Home.name) {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
    }
}