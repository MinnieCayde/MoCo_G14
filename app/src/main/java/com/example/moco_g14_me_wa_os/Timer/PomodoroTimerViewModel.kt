package com.example.moco_g14_me_wa_os.Timer

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

open class PomodoroTimerViewModel(application: Application) : AndroidViewModel(application) {
    private val _timerDurationInMinutes = mutableStateOf(25)
    val timerDurationInMinutes: State<Int> = _timerDurationInMinutes


    private val _timerValueInMilliSeconds = MutableStateFlow(25 * 60 * 1000L)
    open val timerValue: StateFlow<Long> = _timerValueInMilliSeconds

    private val _isRunning = MutableStateFlow(false)
    open val isRunning: StateFlow<Boolean> = _isRunning

    private var timerService: PomodoroTimerService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PomodoroTimerService.LocalBinder
            timerService = binder.getService()
            timerService?.setCallback { time ->
                _timerValueInMilliSeconds.value = time
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerService = null
        }
    }

    init {
        val intent = Intent(getApplication(), PomodoroTimerService::class.java)
        application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    open fun startTimer() {
        timerService?.startTimer()
        _isRunning.value = true
    }

    open fun pauseTimer() {
        timerService?.pauseTimer()
        _isRunning.value = false
    }

    open fun resetTimer() {
        timerService?.resetTimer()
        _isRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unbindService(serviceConnection)
    }
    fun changeTime(newTimeInMinutes:Int){
        _timerDurationInMinutes.value = newTimeInMinutes
        _timerValueInMilliSeconds.value = newTimeInMinutes * 60 * 1000L
    }

}