package com.example.moco_g14_me_wa_os.Settings

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: SettingsRepository): ViewModel() {
    //Flow für Dark Mode
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    //Flow für Beanachrichtigungen
    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled

    //Flow für Worktimer Optionen
    private val _selectedWorkTimer = MutableStateFlow(2)
    val selectedWorkTimer: StateFlow<Int> = _selectedWorkTimer

    init {
        // Einstellungen laden
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            // Dark Mode Einstellung sammeln
            repository.getDarkMode().collect { darkMode ->
                _isDarkMode.value = darkMode
            }
            // Benachrichtigungseinstellungen sammeln
            repository.getNotification().collect { notificationsEnabled ->
                _notificationsEnabled.value = notificationsEnabled
            }
            // Timer Option sammeln
            repository.getWorkTimer().collect { timerOption ->
                _selectedWorkTimer.value = timerOption
            }
        }
    }

    fun toggleDarkmode(isEnabled : Boolean){
        viewModelScope.launch {
            repository.saveDarkmodeSetting(isEnabled)
            _isDarkMode.value = isEnabled
        }
    }

    fun toggleNotifications(isEnabled: Boolean){
        viewModelScope.launch {
            repository.saveNotificationSetting(isEnabled)
            _notificationsEnabled.value = isEnabled
        }
    }

    fun setWorkTimerOption(option: Int){
        viewModelScope.launch {
            repository.saveWorkTimerOption(option)
            _selectedWorkTimer.value = option
        }
    }
}