package com.example.tapbot.ui.screens.home

import android.content.Context
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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tapbot.domain.utils.startTapBotForegroundService
import com.example.tapbot.domain.utils.stopTapBotForegroundService
import com.example.tapbot.domain.utils.triggerClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()) {
    Test()
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
            .background(MaterialTheme.colors.background.copy(alpha = 0.1f))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Number of click", fontSize = 18.sp, color = MaterialTheme.colors.onBackground)

        Spacer(modifier = Modifier.height(5.dp))

        BasicTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(MaterialTheme.colors.onPrimary.copy(alpha = 0.3f))
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

        Text(text = "Duration of delay, in seconds", fontSize = 18.sp, color = MaterialTheme.colors.onBackground)

        Spacer(modifier = Modifier.height(5.dp))

        BasicTextField(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(corner = CornerSize(4.dp)))
                .background(MaterialTheme.colors.onPrimary.copy(alpha = 0.3f))
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
        }) {
            Text(text = "Start", fontSize = 18.sp, color = Color.White)
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
    Column(
        modifier = Modifier
            .height(100.dp)
            .width(400.dp)
            .background(MaterialTheme.colors.background.copy(alpha = 0.4f))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Overlay", fontSize = 18.sp, color = MaterialTheme.colors.onBackground)
    }
}