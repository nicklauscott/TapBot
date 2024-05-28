package com.example.tapbot.ui.screens.settings.state_and_events

sealed class SettingScreenUiEvent {
    object StartService : SettingScreenUiEvent()
    object StopService : SettingScreenUiEvent()
    data class LoadServiceStatus(val isServiceRunning: Boolean) : SettingScreenUiEvent()
    object PermissionDenied : SettingScreenUiEvent()
}