package com.example.tapbot

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tapbot.domain.activityresult.OverlayPermissionContract
import com.example.tapbot.domain.sevices.TapBotForegroundService
import com.example.tapbot.domain.utils.isAccessibilityServiceEnabled
import com.example.tapbot.domain.utils.requestAccessibilityPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var overlayPermissionLauncher: ActivityResultLauncher<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val bg = findViewById<ConstraintLayout>(R.id.main)


        bg.setOnClickListener {
            bg.setBackgroundColor(Color.RED)
        }

        overlayPermissionLauncher = registerForActivityResult(
            OverlayPermissionContract(this)
        ) { isGranted ->
            if (isGranted) {
                startTapBotForegroundService()
            }else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        if (!Settings.canDrawOverlays(this)) {
            overlayPermissionLauncher.launch(Unit)
        }else {
            if (!isAccessibilityServiceEnabled()) {
                requestAccessibilityPermission()
                if (!isAccessibilityServiceEnabled()) {
                    Toast.makeText(this, "Accessibility service is not enabled", Toast.LENGTH_SHORT).show()
                    return
                }
            }
            startTapBotForegroundService()



            Toast.makeText(this@MainActivity, "TapBot is sending", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.IO).launch {
                delay(10000)
                repeat(600) {
                    delay(500)
                    withContext(Dispatchers.Main) {
                        triggerClick(500f, 700f)
                    }
                }
            }

        }
    }

    private fun startTapBotForegroundService() {
        val serviceIntent = Intent(this, TapBotForegroundService::class.java)
        startService(serviceIntent)
    }

    private fun triggerClick(x: Float, y: Float) {
        Log.d("OverlayPermissionContract", "triggerClick: sendBroadcast")
        val intent = Intent("com.example.tapbot.PERFORM_CLICK").apply {
            putExtra("x", x)
            putExtra("y", y)
        }
        sendBroadcast(intent)
    }

//    init {
//        accessibilityPermissionLauncher.contract.parseResult(1, null)
//    }
}