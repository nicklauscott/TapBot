package com.example.tapbot.ui.screens.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
fun cornerRadius(modifier: Modifier = Modifier): Dp{
    return (min(
        LocalConfiguration.current.screenWidthDp,
        LocalConfiguration.current.screenHeightDp,
    ) * 0.05).dp
}

@Composable
fun rectangularModifier(padding: Dp = cornerRadius(), width: Dp? = null, height: Dp? = null,
                        shape: Shape = RoundedCornerShape(4.dp), background: Color = Color.Black
): Modifier{
    val newWidth = if (width == null) Modifier.fillMaxWidth() else Modifier.width(width)
    val newHeight = if (height == null) Modifier.fillMaxHeight() else Modifier.height(height)
    return Modifier
        .padding(padding)
        .then(newWidth)
        .then(newHeight)
        .clip(shape)
        .background(background)
        .padding(padding / 2)
}