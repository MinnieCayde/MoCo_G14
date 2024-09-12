package com.example.moco_g14_me_wa_os.Settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit


val Context.datastore by preferencesDataStore(name = "Settings")

class SettingsRepository (private val context: Context) {
    //Keys für den Datastore
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    private val NOTIFICATION_KEY = booleanPreferencesKey("notifications")
    private val WORK_TIMER_OPTION_KEY = intPreferencesKey("worktimer_option")

    //Settings laden 2
    fun getDarkMode(): Flow<Boolean> {
        return context.datastore.data.map { preferences -> preferences[DARK_MODE_KEY] ?: false }
    }
    fun getNotification(): Flow<Boolean> {
        return context.datastore.data.map { preferences -> preferences[NOTIFICATION_KEY] ?: true }
    }
    fun getWorkTimer(): Flow<Int> {
        return context.datastore.data.map { preferences -> preferences[WORK_TIMER_OPTION_KEY] ?: 25 }
    }

    //Speichern von Darkmode, notifications und SelectedOptions
    suspend fun saveDarkmodeSetting(isEnabled: Boolean){
        context.datastore.edit { preferences -> preferences[DARK_MODE_KEY] = isEnabled }
    }
    suspend fun saveNotificationSetting(isEnabled: Boolean){
        context.datastore.edit { preferences -> preferences[NOTIFICATION_KEY] = isEnabled }
    }
    suspend fun saveWorkTimerOption(option: Int){
        context.datastore.edit { preferences -> preferences[WORK_TIMER_OPTION_KEY] = option}
    }
}