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
import javax.inject.Inject


val Context.datastore by preferencesDataStore(name = "Settings")

class SettingsRepository @Inject constructor(private val context: Context) {
    //Keys für den Datastore
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    private val NOTIFICATION_KEY = booleanPreferencesKey("notifications")
    private val WORK_BREAK_OPTION_KEY = intPreferencesKey("workbreak_option")
    private val SESSION_BREAK_OPTION_KEY = intPreferencesKey("sessionbreak_option")
    private val SESSION_COUNT_KEY = intPreferencesKey("sessionCount")

    //Settings laden 2
    fun getDarkMode(): Flow<Boolean> {
        return context.datastore.data.map { preferences -> preferences[DARK_MODE_KEY] ?: false }
    }
    fun getNotification(): Flow<Boolean> {
        return context.datastore.data.map { preferences -> preferences[NOTIFICATION_KEY] ?: true }
    }
    fun getWorkBreak(): Flow<Int> {
        return context.datastore.data.map { preferences -> preferences[WORK_BREAK_OPTION_KEY] ?: 5}
    }
    fun getSessionBreak(): Flow<Int> {
        return context.datastore.data.map { prefereces -> prefereces[SESSION_BREAK_OPTION_KEY] ?: 15 }
    }
    fun getSessionCount(): Flow<Int> {
        return context.datastore.data.map { preferences -> preferences[SESSION_COUNT_KEY] ?: 1}
    }


    //Speichern von Darkmode, notifications und SelectedOptions
    suspend fun saveDarkmodeSetting(isEnabled: Boolean){
        context.datastore.edit { preferences -> preferences[DARK_MODE_KEY] = isEnabled }
    }
    suspend fun saveNotificationSetting(isEnabled: Boolean){
        context.datastore.edit { preferences -> preferences[NOTIFICATION_KEY] = isEnabled }
    }
    suspend fun saveWorkBreakOption(option: Int){
        context.datastore.edit { preferences -> preferences[WORK_BREAK_OPTION_KEY] = option}
    }
    suspend fun saveSessionBreakOption(option: Int){
        context.datastore.edit { preferences -> preferences[SESSION_BREAK_OPTION_KEY] = option }
    }
    suspend fun saveSessionCount(value : Int){
        context.datastore.edit { preferences -> preferences[SESSION_COUNT_KEY] = value}
    }
}