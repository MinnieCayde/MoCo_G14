//package com.example.moco_g14_me_wa_os
//import TimerScreen
//import android.content.Intent
//import androidx.activity.ComponentActivity
//import androidx.activity.viewModels
//import android.os.Bundle
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.ui.Modifier
//import androidx.core.content.ContextCompat
//import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerService
//import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
//import com.example.moco_g14_me_wa_os.Timer.TimerViewModelFactory
//
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class TimerActivity : ComponentActivity() {
//    private val timerViewModel: PomodoroTimerViewModel by viewModels {
//        TimerViewModelFactory(application) // 30 Minuten in Millisekunden
//     }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            TimerScreen(Modifier.fillMaxSize(1f),timerViewModel)
//
//        }
//        startPomodoroTimerService()
//    }
//    private fun startPomodoroTimerService() {
//        val intent = Intent(this, PomodoroTimerService::class.java)
//        ContextCompat.startForegroundService(this, intent)
//    }
//}