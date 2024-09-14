package com.example.moco_g14_me_wa_os.Timer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimerPreferencesDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timer_preferences")
        private val TIMER_DURATION_KEY = intPreferencesKey("timer_duration")

    }

    val timerDurationFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[TIMER_DURATION_KEY] ?: 25 // Default value is 25 minutes
        }

    suspend fun saveTimerDuration(duration: Int) {
        context.dataStore.edit { preferences ->
            preferences[TIMER_DURATION_KEY] = duration
        }
    }

}