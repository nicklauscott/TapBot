package com.example.tapbot.ui.sevices

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.tapbot.ui.screens.home.OverlayTapBot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TapBotAccessibilityService: AccessibilityService() {

    private var composeView: ComposeView? = null
    private lateinit var windowManager: WindowManager

    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val x = intent?.getFloatExtra("x", 0f)
            val y = intent?.getFloatExtra("y", 0f)
            if ((x != null && x != -1f) && (y != null && y != -1f)) {
                removeOverlay()
                performClick(x, y)
            }
        }
    }

    private val loadConfigReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val x = intent?.getStringExtra("text")
            if (x != null && x != " ") {
                showOverlay()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onServiceConnected() {
        super.onServiceConnected()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val actionIntent = IntentFilter("com.example.tapbot.PERFORM_CLICK")
        registerReceiver(actionReceiver, actionIntent, RECEIVER_NOT_EXPORTED)

        val loadConfigIntent = IntentFilter("com.example.tapbot.LOAD.CONFIG")
        registerReceiver(loadConfigReceiver, loadConfigIntent, RECEIVER_NOT_EXPORTED)
    }

    private fun showOverlay() {
        composeView = ComposeView(this).apply {
            setContent {
                OverlayTapBot()
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT
        )

        // Trick the ComposeView into thinking we are tracking lifecycle
        val lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        composeView!!.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView!!.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        windowManager.addView(composeView, params)

    }

    private fun removeOverlay() {
        composeView?.let {
            windowManager.removeView(it)
            composeView = null
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()
        removeOverlay()
        unregisterReceiver(actionReceiver)
        unregisterReceiver(loadConfigReceiver)
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