package com.example.tapbot.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView



private val DarkColorScheme = darkColorScheme(
    background = background_Dark,
    onBackground = onBackground_Dark,
    onSurface = onSurface_Dark,
    primary = primary_Dark,
    onPrimary = onPrimary_Dark,
    onTertiaryContainer = onTertiaryContainer_Dark,
    primaryContainer = onPrimary_Dark,
    secondaryContainer = onSecondary_Dark,
    secondary = secondary_Dark,
    tertiary = tertiary_Dark,
    onTertiary = onTertiary_Dark,
    inverseSurface = taskBg_Dark,
    inverseOnSurface = onTaskBg_Dark,
    inversePrimary = switchTrack_Dark,
    surfaceTint = switchBorderStroke_Dark,
    error = error_Dark,
    onError = onError_Dark

    /*
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
     */
)

private val LightColorScheme = lightColorScheme(
    background = background_Light,
    onBackground = onBackground_Light,
    onSurface = onSurface_Light,
    primary = primary_Light,
    onPrimary = onPrimary_Light,
    onTertiaryContainer = onTertiaryContainer_Light,
    primaryContainer = onPrimary_Light,
    secondaryContainer = onSecondary_Light,
    secondary = secondary_Light,
    tertiary = tertiary_Light,
    onTertiary = onTertiary_Light,
    inverseSurface = taskBg_Light,
    inverseOnSurface = onTaskBg_Light,
    inversePrimary = switchTrack_Light,
    surfaceTint = switchBorderStroke_Light,
    error = error_Light,
    onError = onError_Light


    /*
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
     */

    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


@Composable
fun TapBotTheme(
    windowSizeClass: WindowSizeClass,
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()
        if (!darkTheme) window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> window.navigationBarColor = colorScheme.tertiary.toArgb()
            WindowWidthSizeClass.Medium -> window.navigationBarColor = colorScheme.tertiary.toArgb()
            WindowWidthSizeClass.Expanded -> window.navigationBarColor = colorScheme.background.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}