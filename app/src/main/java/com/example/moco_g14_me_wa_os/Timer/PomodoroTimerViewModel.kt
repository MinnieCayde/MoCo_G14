package com.example.moco_g14_me_wa_os.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.moco_g14_me_wa_os.Settings.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
@HiltViewModel
open class PomodoroTimerViewModel @Inject constructor(application: Application, private val settingsRepository: SettingsRepository) : AndroidViewModel(application) {

    private val dataStore = TimerPreferencesDataStore(application)


    private val _state = MutableStateFlow<State>(State.Work)
    val state = _state.asStateFlow()

    private val _totalTime = MutableStateFlow(25 * 60 * 1000L)
    val totalTime = _totalTime.asStateFlow()

    val _remainingTime = MutableStateFlow(25*60*1000L)
    val remainingTime: StateFlow<Long> = _remainingTime


    private val _timerValueInMilliSeconds = MutableStateFlow(25 * 60 * 1000L)


    val _isRunning = MutableStateFlow(false)
    open val isRunning: StateFlow<Boolean> = _isRunning


    private val _completedPomodoros = MutableStateFlow(0)
    val completedPomodoros : StateFlow<Int> = _completedPomodoros


    private val _workBreak = MutableStateFlow(5 * 60 * 1000L) // Default: 25 min in ms
    val workBreak: StateFlow<Long> = _workBreak.asStateFlow()

    private val _sessionBreak = MutableStateFlow(25 * 60 * 1000L) // Default: 25 min in ms
    val sessionBreak: StateFlow<Long> = _sessionBreak.asStateFlow()

    private val _workTime = MutableStateFlow(25 * 60 * 1000L)
    val workTime = _workTime.asStateFlow()

    var timerService: PomodoroTimerService? = null




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
        viewModelScope.launch {
            settingsRepository.getSessionBreak().collect {SessionBreakMinutes ->
                _sessionBreak.value = SessionBreakMinutes.toLong() * 60 *1000
            }
        }
        viewModelScope.launch {
            settingsRepository.getWorkBreak().collect { workBreakMinutes ->
                _workBreak.value = workBreakMinutes.toLong() *60 *1000
            }
        }
        viewModelScope.launch {
            collectTime()
        }


        _totalTime.value = _workTime.value
        val intent = Intent(getApplication(), PomodoroTimerService::class.java)
        application.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    suspend fun collectTime() {
        dataStore.timerDurationFlow.collect { _workTime.value = it.toLong() * 60 * 1000 }
    }
    open fun startTimer() {
        timerService?.startTimer(_totalTime, this)
        _isRunning.value = true
    }

    open fun pauseTimer() {
        timerService?.pauseTimer(this)
        _isRunning.value = false
    }

    open fun resetTimer() {
        timerService?.resetTimer(this)
        _isRunning.value = false
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unbindService(serviceConnection)
    }
    open fun onTimerComplete() {
        _isRunning.value = false
        if(state.value == State.Work){
            _completedPomodoros.value++
            if (isLongBreakRequired()) _state.value = State.Longbreak
            else _state.value = State.Shortbreak
        }
        else {
            _state.value = State.Work
        }


        val nextTimerDuration = when (state.value){
            State.Work -> workTime.value
            State.Longbreak -> sessionBreak.value
            State.Shortbreak -> workBreak.value
        }
        _totalTime.value = nextTimerDuration
        _remainingTime.value = nextTimerDuration

    }



    // Logik zur Entscheidung, ob eine lange Pause erforderlich ist (kann angepasst werden)
    private fun isLongBreakRequired():  Boolean {
        if (completedPomodoros.value == 4)
        {_completedPomodoros.value = _completedPomodoros.value-4
            return true}

        return false
    }
    fun setWorkDuration(minutes: Int) {
        viewModelScope.launch {
                dataStore.saveTimerDuration(minutes)
                _totalTime.value = minutes *60L * 1000L
                _remainingTime.value = _totalTime.value
                collectTime()
        }
    }

    fun updateRemainingTime(newTime: Long) {
        _remainingTime.value = newTime
    }
}

enum class State{
    Shortbreak,
    Longbreak,
    Work
}