package com.example.tapbot.data.sevices.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun Int.percentOfScreenWidth(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return (this * screenWidth / 100).dp
}

@Composable
fun Double.percentOfScreenWidth(): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return (this * screenWidth.toDouble() / 100.0).dp
}

@Composable
fun Int.percentOfScreenHeight(): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return (this * screenHeight / 100).dp
}

@Composable
fun Double.percentOfScreenHeight(): Dp {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    return (this * screenHeight.toDouble() / 100.0).dp
}


@Composable
fun cornerRadius(modifier: Modifier = Modifier): Dp {
    return (min(
        LocalConfiguration.current.screenWidthDp,
        LocalConfiguration.current.screenHeightDp,
    ) * 0.05).dp
}