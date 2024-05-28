package com.example.tapbot.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tapbot.R
import com.example.tapbot.ui.navigation.Navigation
import com.example.tapbot.ui.theme.TapBotTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
        val composeView = findViewById<ComposeView>(R.id.composeView)
        composeView.setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = this)
            TapBotTheme(windowSizeClass) {
                Navigation(windowSizeClass)
            }
        }
    }
}