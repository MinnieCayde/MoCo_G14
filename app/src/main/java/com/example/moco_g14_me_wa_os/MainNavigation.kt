package com.example.moco_g14_me_wa_os

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moco_g14_me_wa_os.Todo.TodoScreen
import com.example.moco_g14_me_wa_os.Todo.TodoViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "todo") {
        composable("todo") {
            val todoViewModel: TodoViewModel = hiltViewModel()
            TodoScreen(navController, todoViewModel)
        }
       /*
        composable("timer"){
            val timerViewModel: TimerViewModel = hiltViewModel()
            TimerScreen(navController, timerViewModel)
        }
        composable("settings"){
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(navController, settingsViewModel)
        }
        */
    }
}