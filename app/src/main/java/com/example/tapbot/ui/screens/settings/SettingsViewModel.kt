package com.example.tapbot.ui.screens.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tapbot.ui.screens.settings.state_and_events.SettingScreenUiEvent
import com.example.tapbot.ui.screens.settings.state_and_events.SettingsScreenUIState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {

    private val _state: MutableState<SettingsScreenUIState> = mutableStateOf(SettingsScreenUIState())
    val state: State<SettingsScreenUIState> = _state

    private val _channel = Channel<SettingsChannel>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    fun onEvent(event: SettingScreenUiEvent) {
        when (event) {
            SettingScreenUiEvent.StartService ->  _state.value = state.value.copy(isServiceRunning = true)
            SettingScreenUiEvent.StopService ->  _state.value = state.value.copy(isServiceRunning = false)
            is SettingScreenUiEvent.LoadServiceStatus -> {
                if (state.value.loading == null)
                    _state.value = state.value.copy(loading = false, isServiceRunning = event.isServiceRunning)
                else
                    _state.value = state.value.copy(isServiceRunning = event.isServiceRunning)
            }

            SettingScreenUiEvent.PermissionDenied -> {
                viewModelScope.launch {
                    _channel.send(SettingsChannel.PermissionDenied)
                }
            }
        }
    }

    sealed class SettingsChannel {
        object PermissionDenied: SettingsChannel()
    }
}