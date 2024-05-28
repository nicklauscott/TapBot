package com.example.tapbot.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth
import com.example.tapbot.ui.screens.util.rectangularModifier
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(windowSizeClass: WindowSizeClass) {
    val viewModel: SettingsViewModel = hiltViewModel()

    val snackBarHostState = remember { SnackbarHostState() }
    val errorMessage = remember { mutableStateOf("") }

    LaunchedEffect(true) {
        viewModel.channel.collectLatest {
            when (it) {
                SettingsViewModel.SettingsChannel.PermissionDenied -> {
                    errorMessage.value = "Permission denied"
                    snackBarHostState.showSnackbar("Permission denied")
                }
            }
        }
    }

    Scaffold(
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
            WindowWidthSizeClass.Compact -> SettingScreenPortrait(viewModel)
            WindowWidthSizeClass.Medium -> SettingScreenPortrait(viewModel)
            WindowWidthSizeClass.Expanded -> SettingScreenLandscape(viewModel)
            else -> SettingScreenPortrait(viewModel)
        }
    }

}




