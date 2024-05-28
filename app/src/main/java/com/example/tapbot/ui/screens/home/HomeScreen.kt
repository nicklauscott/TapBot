package com.example.tapbot.ui.screens.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tapbot.domain.usecases.services.ForegroundService
import com.example.tapbot.domain.utils.broadcastLoadConfig
import com.example.tapbot.domain.utils.startTapBotForegroundService
import com.example.tapbot.domain.utils.stopTapBotForegroundService
import com.example.tapbot.domain.utils.triggerClick
import com.example.tapbot.ui.screens.home.components.AppDestinations
import com.example.tapbot.ui.screens.home.components.calculateLayoutType
import com.example.tapbot.ui.screens.info.InfoScreen
import com.example.tapbot.ui.screens.settings.SettingsScreen
import com.example.tapbot.ui.screens.settings.SettingsViewModel
import com.example.tapbot.ui.screens.settings.state_and_events.SettingScreenUiEvent
import com.example.tapbot.ui.screens.tasks.TasksScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)
@Composable
fun HomeScreen(windowSizeClass: WindowSizeClass, navController: NavController) {

    val context  = LocalContext.current
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.TASKS) }

    val askedForPermission = rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK || Settings.canDrawOverlays(context)) {
            ForegroundService.startService(context)
        }
    }

    LaunchedEffect(askedForPermission) {
        if (!Settings.canDrawOverlays(context) && !askedForPermission.value) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
            launcher.launch(intent)
        }
        askedForPermission.value = true
    }

    NavigationSuiteScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationRailContainerColor = MaterialTheme.colorScheme.tertiary,
            navigationBarContainerColor = MaterialTheme.colorScheme.tertiary,
            navigationBarContentColor = MaterialTheme.colorScheme.primaryContainer

        ),
        layoutType = calculateLayoutType(windowSizeClass),
        navigationSuiteItems = {
            AppDestinations.values().forEach {
                item(icon = {
                    Image(painter = painterResource(id = it.imageResource),
                        contentDescription = stringResource(id = it.contentDescription),
                        colorFilter = if (currentDestination.name == it.name) ColorFilter.tint(
                            color = MaterialTheme.colorScheme.onTertiary
                        ) else ColorFilter.tint(
                            color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.8f)
                        ))
                },
                    label = { Text(text = stringResource(id = it.label),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (currentDestination.name == it.name) MaterialTheme.colorScheme.onTertiary
                        else MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.8f))
                         },
                    selected = currentDestination == it,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        when (currentDestination) {
            AppDestinations.TASKS -> TasksScreen(windowSizeClass)
            AppDestinations.SETTINGS -> SettingsScreen(windowSizeClass)
            AppDestinations.INFO -> InfoScreen()
        }
    }

}



@Composable
fun Test(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val isForegroundActive = remember { mutableStateOf(true) }

    val number = remember { mutableStateOf("") }
    val delay = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.1f))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Number of click", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(5.dp))

        BasicTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))
                .padding(vertical = 10.dp, horizontal = 5.dp),
            value = number.value,
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                if (it.length <= 3) number.value = it
        })

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Duration of delay, in seconds", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(5.dp))

        BasicTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))
                .padding(vertical = 10.dp, horizontal = 5.dp),
            value = delay.value,
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                if (it.length <= 2) delay.value = it
            })

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            if (number.value.isEmpty() || delay.value.isEmpty()) return@Button
            scope.launch(Dispatchers.IO) {
                delay(delay.value.toInt() * 1000L)
                work(context, number.value.toInt())

            }
            //context.broadcastLoadConfig("Welcome")
        }) {
            Text(text = "Load", fontSize = 18.sp, color = Color.White)
        }


        Spacer(modifier = Modifier.height(45.dp))

        Button(onClick = {
            if (isForegroundActive.value) {
                stopTapBotForegroundService(context)
            }else {
                startTapBotForegroundService(context)
            }
        }) {
            Text(text = if (isForegroundActive.value) "Stop service" else "Start Service",
                fontSize = 18.sp, color = Color.White)
        }
    }
}

fun CoroutineScope.work(context: Context, number: Int) {
    launch(Dispatchers.IO) {
        while (true) {
            withContext(Dispatchers.Main) {
                repeat(number) {
                    delay(30)
                    context.triggerClick(500f, 700f)
                }
            }
            delay(480000)
        }
    }
}


@Composable
fun OverlayTapBot(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .height(100.dp)
            .width(400.dp)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.4f))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Overlay", fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Log.d("SplashScreenViewModel.SplashScreenUiChannel", "OverlayTapBot: Start")
            context.triggerClick(500f, 700f)
        }) {
            Text(text = "Start", fontSize = 18.sp, color = Color.White)
        }
    }
}