package com.example.tapbot.domain.sevices

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.tapbot.R
import com.example.tapbot.domain.utils.isAccessibilityServiceEnabled
import com.example.tapbot.ui.MainActivity
import com.example.tapbot.ui.screens.home.OverlayTapBot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TapBotAccessibilityService: AccessibilityService() {

    private var composeView: ComposeView? = null
    private lateinit var windowManager: WindowManager
    //private val lifecycleRegistry = LifecycleRegistry(this)

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
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //lifecycleRegistry.currentState = Lifecycle.State.STARTED
        showOverlay()
        val filter = IntentFilter("com.example.tapbot.PERFORM_CLICK")
        registerReceiver(clickReceiver, filter, RECEIVER_NOT_EXPORTED)
        Log.d("MyAccessibilityService", "Service connected and receiver registered.")
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
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )

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
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (isAccessibilityServiceEnabled()) {
                Log.d("MyAccessibilityService", "AccessibilityEvent enable")
            }
        }
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {
        super.onDestroy()
        removeOverlay()
        //lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
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