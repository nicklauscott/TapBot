package com.example.tapbot.domain.usecases.services

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.example.tapbot.domain.sevices.TapBotForegroundService

object ForegroundService {
    fun startService(context: Context): Boolean {
        val serviceIntent = Intent(context, TapBotForegroundService::class.java)
        context.startService(serviceIntent)
        return checkServiceRunning(context)
    }

    fun stopService(context: Context): Boolean {
        val serviceIntent = Intent(context, TapBotForegroundService::class.java)
        context.stopService(serviceIntent)
        return !checkServiceRunning(context)
    }

    @SuppressLint("ServiceCast")
    fun checkServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (TapBotForegroundService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

}