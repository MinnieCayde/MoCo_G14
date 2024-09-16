package com.example.moco_g14_me_wa_os

import TimerScreen
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.moco_g14_me_wa_os.Settings.SettingsScreen
import com.example.moco_g14_me_wa_os.Timer.PomodoroTimerService
import com.example.moco_g14_me_wa_os.Todo.TodoScreen
import kotlinx.coroutines.launch

@Composable
fun MainNavigation() {

    val pagerState = rememberPagerState(1) { 3 }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        HorizontalPager(
            beyondViewportPageCount = 3,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> LeftScreen()
                1 -> MiddleScreen()
                2 -> RightScreen()
            }
        }


        // Buttons at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.onSecondary,
                    RoundedCornerShape(16.dp)
                )
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {

            Button(modifier = Modifier.padding(8.dp),
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(0) } }) {
                Text(text = "Todo")
            }
            Button(modifier = Modifier.padding(8.dp),
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(1) } }) {
                Text(text = "Timer")
            }
            Button(modifier = Modifier.padding(8.dp),
                onClick = { coroutineScope.launch { pagerState.animateScrollToPage(2) } }) {
                Text(text = "Settings")
            }
        }
    }
}

@Composable
fun LeftScreen() {
    TodoScreen()
}

@Composable
fun MiddleScreen() {
    val context = LocalContext.current
    TimerScreen()
    LaunchedEffect(Unit) {
        startTimerService(context)
    }
}

private fun startTimerService(context: android.content.Context) {
    val intent = Intent(context, PomodoroTimerService::class.java)
    ContextCompat.startForegroundService(context, intent)
}

@Composable
fun RightScreen() {
    SettingsScreen()
}