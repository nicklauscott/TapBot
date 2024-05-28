package com.example.tapbot.ui.screens.home.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.tapbot.R

enum class AppDestinations(
    @StringRes val label: Int,
    @DrawableRes val imageResource: Int,
    @StringRes val contentDescription: Int
) {
    TASKS(R.string.tasks, R.drawable.task_icon, R.string.tasks),
    SETTINGS(R.string.settings, R.drawable.setting_icon, R.string.settings),
    INFO(R.string.help, R.drawable.help_icon, R.string.help),
}


@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
fun calculateLayoutType(windowSizeClass: WindowSizeClass): NavigationSuiteType {
    return with(windowSizeClass) {
        if (widthSizeClass == WindowWidthSizeClass.Compact) {
            NavigationSuiteType.NavigationBar
        }else if (heightSizeClass == WindowHeightSizeClass.Expanded &&
            widthSizeClass == WindowWidthSizeClass.Expanded) {
            NavigationSuiteType.NavigationDrawer
        } else {
            NavigationSuiteType.NavigationRail
        }
    }
}
