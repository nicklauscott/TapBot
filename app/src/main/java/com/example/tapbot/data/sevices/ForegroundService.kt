package com.example.tapbot.data.sevices

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tapbot.domain.model.Action
import com.example.tapbot.domain.model.ClickTask
import com.example.tapbot.domain.model.DelayTask
import com.example.tapbot.domain.model.IndividualTask
import com.example.tapbot.domain.model.LoopTask
import com.example.tapbot.domain.model.StartLoop
import com.example.tapbot.domain.model.StopLoopCondition
import com.example.tapbot.domain.model.StopLoop
import com.example.tapbot.domain.usecases.taskbuilder.TaskManager
import com.example.tapbot.domain.utils.triggerClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object ForegroundService {
    inline fun <reified T> startService(context: Context): Boolean {
        val serviceIntent = Intent(context, T::class.java)
        context.startService(serviceIntent)
        return checkServiceRunning<TapBotForegroundService>(context)
    }

    inline fun <reified T> stopService(context: Context): Boolean {
        val serviceIntent = Intent(context, T::class.java)
        context.stopService(serviceIntent)
        return !checkServiceRunning<TapBotForegroundService>(context)
    }

    fun accessibilityRequest(context: Context) {
        val intent = Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }

    @SuppressLint("ServiceCast")
    inline fun <reified T> checkServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (T::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

}