package com.example.moco_g14_me_wa_os.Todo

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint


class TodoActivity: ComponentActivity() {
    // Get the ViewModel instance using viewModels delegate
    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory(application) // Using a factory to pass the application context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Pass the ViewModel to the Composable
            //TodoApp(todoViewModel)
        }
    }
}

