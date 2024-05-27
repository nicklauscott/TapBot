package com.example.tapbot.domain.utils

import android.content.Context
import android.content.Intent
import com.example.tapbot.domain.sevices.TapBotForegroundService

fun startTapBotForegroundService(context: Context) {
    val serviceIntent = Intent(context, TapBotForegroundService::class.java)
    context.startService(serviceIntent)
}

fun stopTapBotForegroundService(context: Context) {
    val serviceIntent = Intent(context, TapBotForegroundService::class.java)
    context.stopService(serviceIntent)
}

fun Context.triggerClick(x: Float, y: Float) {
    val intent = Intent("com.example.tapbot.PERFORM_CLICK").apply {
        putExtra("x", x)
        putExtra("y", y)
    }
    sendBroadcast(intent)
}

fun Context.broadcastLoadConfig(text: String) {
    val intent = Intent("com.example.tapbot.LOAD.CONFIG").apply {
        putExtra("text", text)
    }
    sendBroadcast(intent)
}