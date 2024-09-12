package com.example.moco_g14_me_wa_os


import TimerScreen
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moco_g14_me_wa_os.Todo.TododScreen
import com.example.moco_g14_me_wa_os.Todo.TodoViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.moco_g14_me_wa_os.Settings.SettingsScreen
import com.example.moco_g14_me_wa_os.Settings.SettingsViewModel
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerService
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerViewModel
import kotlinx.coroutines.launch

@Composable
fun MainNavigation() {

    val navController = rememberNavController()
    val pagerState = rememberPagerState(1) { 3 }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            beyondViewportPageCount = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> LeftScreen(navController = navController)       // Left screen
                1 -> MiddleScreen(navController = navController)  // Middle screen
                2 -> RightScreen(navController = navController)   // Right screen
            }
        }
        // Buttons at the bottom
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) }}){
                Text(text = "Todo")
            }
            Button(onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) }}) {
                Text(text = "Timer")
            }
            Button(onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) }}) {
                Text(text = "Settings")
            }
        }
    }
}


@Composable
fun LeftScreen(navController: NavController){
    val todoViewModel: TodoViewModel = hiltViewModel()

    TododScreen(todoViewModel = todoViewModel)

}

@Composable
fun MiddleScreen(navController: NavController) {
    val timerViewModel: PomodoroTimerViewModel = hiltViewModel()
    val context = LocalContext.current
    TimerScreen(timerViewModel = timerViewModel)
    LaunchedEffect(Unit) {
        startTimerService(context)
    }
}
private fun startTimerService(context: android.content.Context){
    val intent = Intent(context, PomodoroTimerService::class.java)
    ContextCompat.startForegroundService(context, intent)
}


@Composable
fun RightScreen(navController: NavController) {
    val settingsViewModel : SettingsViewModel = hiltViewModel()

    SettingsScreen(viewModel = settingsViewModel)
}