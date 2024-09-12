package com.example.moco_g14_me_wa_os.Timer

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moco_g14_me_wa_os.MainActivity
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import com.squareup.wire.get

class PomodoroTimerService : Service() {
    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis = 30*60*1000L
    private var isTimerRunning = false
    private var callback: ((Long) -> Unit)? = null

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): PomodoroTimerService = this@PomodoroTimerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification("")
        startForeground(NOTIFICATION_ID, notification)

        return START_STICKY
    }

    fun setCallback(cb: (Long) -> Unit) {
        callback = cb
    }

    fun startTimer() {
        if (!isTimerRunning) {
            timer = object : CountDownTimer(timeLeftInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    callback?.invoke(timeLeftInMillis)
                    updateNotification()
                }

                override fun onFinish() {
                    isTimerRunning = false
                    updateNotification("Pomodoro finished!")
                    stopSelf() //stop service once timer finished
                }
            }.start()

            isTimerRunning = true
            updateNotification()
        }
    }

    fun pauseTimer() {
        if (isTimerRunning) {
            timer.cancel()
            isTimerRunning = false
            updateNotification()
        }
    }

    fun resetTimer() {
        if (::timer.isInitialized) {
            timer.cancel()
        }
        timeLeftInMillis = 25 * 60 * 1000L
        isTimerRunning = false
        callback?.invoke(timeLeftInMillis)
        updateNotification()
    }

    private fun updateNotification(contentText: String = "Timer is running") {
        val notification = createNotification(contentText)
        startForeground(NOTIFICATION_ID, notification)
    }
    private fun createNotification(contentText: String): Notification {
        // Verwenden Sie den vollständigen Paketnamen für MainActivity
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
        private const val NOTIFICATION_ID = 1
    }
}
