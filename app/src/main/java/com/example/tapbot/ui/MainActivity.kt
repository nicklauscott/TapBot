package com.example.tapbot.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tapbot.R
import com.example.tapbot.domain.activityresult.OverlayPermissionContract
import com.example.tapbot.domain.sevices.TapBotForegroundService
import com.example.tapbot.domain.utils.isAccessibilityServiceEnabled
import com.example.tapbot.domain.utils.requestAccessibilityPermission
import com.example.tapbot.ui.navigation.Navigation
import com.example.tapbot.ui.theme.TapBotTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var overlayPermissionLauncher: ActivityResultLauncher<Unit>

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        overlayPermissionLauncher = registerForActivityResult(
            OverlayPermissionContract(this)
        ) { isGranted ->
            if (isGranted) {
                startTapBotForegroundService()
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
        }

        val composeView = findViewById<ComposeView>(R.id.composeView)

        composeView.setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            TapBotTheme {
                Navigation(windowSizeClass)
            }
        }
    }

    private fun startTapBotForegroundService() {
        val serviceIntent = Intent(this, TapBotForegroundService::class.java)
        startService(serviceIntent)
    }
}