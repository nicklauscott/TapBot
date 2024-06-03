package com.example.tapbot.ui.screens.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.tapbot.R
import com.example.tapbot.data.sevices.ForegroundService
import com.example.tapbot.data.sevices.TapBotForegroundService
import com.example.tapbot.ui.screens.settings.state_and_events.SettingScreenUiEvent
import com.example.tapbot.ui.screens.settings.widgets.EnableMainFeature
import com.example.tapbot.ui.screens.settings.widgets.SettingCell
import com.example.tapbot.ui.screens.settings.widgets.SettingItem
import com.example.tapbot.ui.screens.util.percentOfScreenHeight
import com.example.tapbot.ui.screens.util.percentOfScreenWidth

@Composable
fun SettingScreenPortrait(viewModel: SettingsViewModel) {

    val context  = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK || Settings.canDrawOverlays(context)) {
            ForegroundService.startService<TapBotForegroundService>(context).also {
                viewModel.onEvent(SettingScreenUiEvent.LoadServiceStatus(it))
            }
        }else{
            viewModel.onEvent(SettingScreenUiEvent.PermissionDenied)
        }
    }


    LaunchedEffect(viewModel) {
        ForegroundService.checkServiceRunning<TapBotForegroundService>(context).also {
            viewModel.onEvent(SettingScreenUiEvent.LoadServiceStatus(it))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.height(20.percentOfScreenHeight()))

        Column(modifier = Modifier.padding(
            start = 4.percentOfScreenWidth(),
            end = 4.percentOfScreenWidth())) {

            Text(text = "Settings", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
        }

        Spacer(modifier = Modifier.height(2.percentOfScreenHeight()))

        EnableMainFeature(
            modifier = Modifier.padding(start = 4.percentOfScreenWidth(), end = 4.percentOfScreenWidth()),
            isEnabled = viewModel.state.value.isServiceRunning) {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
                launcher.launch(intent)
                return@EnableMainFeature
            }

            if (viewModel.state.value.isServiceRunning) {
                ForegroundService.stopService<TapBotForegroundService>(context).also {
                    if (it) viewModel.onEvent(SettingScreenUiEvent.StopService)
                }
                return@EnableMainFeature
            }

            ForegroundService.startService<TapBotForegroundService>(context).also {
                if (it) viewModel.onEvent(SettingScreenUiEvent.StartService)
            }
        }

        Spacer(modifier = Modifier.height(2.percentOfScreenHeight()))

        SettingCell(settingItem = SettingItem(
            R.drawable.task_icon, "Low Priority Tasks",
            "Run your tasks in the background to save battery and time Run your tasks in the background to save battery and time ")
        ) {

        }

        SettingCell(settingItem = SettingItem(
            R.drawable.task_icon_2, "Low Priority Tasks",
            "Run your tasks in the background to save battery and time Run your tasks in the background to save battery and time ")
        ) {

        }
    }
}