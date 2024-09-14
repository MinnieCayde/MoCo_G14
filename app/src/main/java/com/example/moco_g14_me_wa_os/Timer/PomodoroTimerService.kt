package com.example.moco_g14_me_wa_os.Timer

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.moco_g14_me_wa_os.MainActivity
import kotlinx.coroutines.Job
import android.app.Service
import android.content.ContentValues.TAG
import android.media.RingtoneManager
import android.util.Log
import com.example.moco_g14_me_wa_os.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PomodoroTimerService : Service() {
    private var callback: ((Long) -> Unit)? = null

    private val binder = LocalBinder()
    private var ispaused :Boolean = false
    var duration = 0L
    private var timerJob: Job? = null
    private lateinit var dataStore: TimerPreferencesDataStore

    inner class LocalBinder : Binder() {
        fun getService(): PomodoroTimerService = this@PomodoroTimerService
    }
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate called")
        dataStore = TimerPreferencesDataStore(applicationContext)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand called")
        startForeground(NOTIFICATION_ID,createNotification("Timer service started"))
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "Service onBind called")
        return binder
    }

    fun setCallback(cb: (Long) -> Unit) {
        callback = cb
    }

    fun startTimer(totaltime : MutableStateFlow<Long>, viewModel : PomodoroTimerViewModel) {
        if (!viewModel.isRunning.value && timerJob == null) {
            viewModel._isRunning.value = true
            updateNotification("Timer started")
            timerJob = CoroutineScope(Dispatchers.Default).launch {
                if (!ispaused) {
                    duration = totaltime.value
                    viewModel._remainingTime.value = duration
                }
                while (viewModel._remainingTime.value > 0) {
                    delay(1000)
                    viewModel._remainingTime.value -= 1000
                    withContext(Dispatchers.Main) {
                        callback?.invoke(viewModel._remainingTime.value)
                    }
                    updateNotification("Remaining time: ${viewModel._remainingTime.value / 1000} seconds")
                }
                viewModel.onTimerComplete()
                sendTimerCompletionNotification()
                timerJob = null

            }
        }
    }

    fun pauseTimer(viewModel: PomodoroTimerViewModel) {
        if (viewModel.isRunning.value){
        timerJob?.cancel()
        viewModel._isRunning.value= false
        timerJob = null
        ispaused = true
        updateNotification("Timer paused")}
    }

    fun resetTimer(viewModel: PomodoroTimerViewModel) {
        pauseTimer(viewModel)
        CoroutineScope(Dispatchers.Default).launch {
            val duration = dataStore.timerDurationFlow.first()
            viewModel._remainingTime.value = duration * 60 * 1000L
            callback?.invoke(viewModel._remainingTime.value)
        }
    }



    private fun updateNotification(contentText: String = "Timer is running") {
        Log.d(TAG, "Updating notification: '$contentText'")
        val notification = createNotification(contentText)
        startForeground(NOTIFICATION_ID, notification)
        Log.d(TAG, "Notification updated and startForeground called with content: '$contentText'")
    }
    private fun createNotification(contentText: String): Notification {
        Log.d(TAG, "Creating notification with content: '$contentText'")
        val channelID = getString(R.string.pomodoro_timer_channel_id)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "pomodoro_timer_channel"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro Timer")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .build()
    }

    companion object {
        private const val TAG = "PomodoroTimerService"
        private const val NOTIFICATION_ID = 1
    }
    private fun sendTimerCompletionNotification() {
        Log.d(TAG, "PomodoroCompleted")
        val channelID = getString(R.string.pomodoro_timer_completion_channel_id)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "pomodoro_timer_channel"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_HIGH // Wichtigere Benachrichtigung
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro Timer Completed")
            .setContentText("Good job! Time for a break.")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true) // Schließt die Benachrichtigung automatisch nach dem Tippen
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification) // Sende die Benachrichtigung mit einer neuen ID
    }
}
