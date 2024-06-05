package com.example.tapbot.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tapbot.ui.screens.home.HomeScreen
import com.example.tapbot.ui.screens.splash.SplashScreen

@Composable
fun Navigation(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Splash.name) {

        composable(route = Screens.Home.name) {
            HomeScreen(windowSizeClass = windowSizeClass, navController = navController)
        }

        composable(route = Screens.Splash.name) {
            SplashScreen(navController = navController)
        }
    }
}