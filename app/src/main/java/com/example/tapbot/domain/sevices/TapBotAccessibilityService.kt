package com.example.tapbot.domain.sevices

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.example.tapbot.domain.utils.isAccessibilityServiceEnabled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TapBotAccessibilityService: AccessibilityService() {

    private val clickReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val x = intent?.getFloatExtra("x", 0f)
            val y = intent?.getFloatExtra("y", 0f)
            if ((x != null && x != -1f) && (y != null && y != -1f)) {
                performClick(x, y)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected() {
        super.onServiceConnected()
        val filter = IntentFilter("com.example.tapbot.PERFORM_CLICK")
        registerReceiver(clickReceiver, filter, RECEIVER_NOT_EXPORTED)
        Log.d("MyAccessibilityService", "Service connected and receiver registered.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (isAccessibilityServiceEnabled()) { // isAccessibilityServiceEnabled()
                Log.d("MyAccessibilityService", "AccessibilityEvent enable")
            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(clickReceiver)
        Log.d("MyAccessibilityService", "Service destroyed and receiver unregistered.")
    }

    fun performClick(x: Float, y: Float) {
        val path = Path()
        path.moveTo(x, y)

        val gestureBuilder = GestureDescription.Builder()

        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))

        val gesture = gestureBuilder.build()

        CoroutineScope(Dispatchers.Main).launch {
            dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    super.onCompleted(gestureDescription)
                    Log.d("MyAccessibilityService", "Gesture completed.")
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    super.onCancelled(gestureDescription)
                    Log.d("MyAccessibilityService", "Gesture cancelled.")
                }
            }, null)
        }
    }
}