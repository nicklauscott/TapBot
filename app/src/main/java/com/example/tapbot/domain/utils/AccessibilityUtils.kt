package com.example.tapbot.domain.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.view.accessibility.AccessibilityManagerCompat
import com.example.tapbot.data.sevices.TapBotAccessibilityService


fun Context.requestAccessibilityPermission() {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    startActivity(intent)
}

fun Context.isAccessibilityServiceEnabled(): Boolean {
    val serviceInfo = ComponentName(this, TapBotAccessibilityService::class.java)
    val enabledServices = AccessibilityUtils.getEnabledAccessibilityServices(this)

    return enabledServices.any { it == serviceInfo }
}

object AccessibilityUtils {
    fun getEnabledAccessibilityServices(context: Context): List<ComponentName> {
        val enabledServices: MutableList<ComponentName> = ArrayList()
        val accessibilityManager = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val installedAccessibilityServices = AccessibilityManagerCompat.getInstalledAccessibilityServiceList(accessibilityManager)

        for (serviceInfo in installedAccessibilityServices) {
            if (serviceInfo.resolveInfo.serviceInfo.enabled) {
                enabledServices.add(ComponentName(serviceInfo.resolveInfo.serviceInfo.packageName, serviceInfo.resolveInfo.serviceInfo.name))
            }
        }
        return enabledServices
    }
}
