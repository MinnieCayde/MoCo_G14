package com.example.moco_g14_me_wa_os
import com.example.moco_g14_me_wa_os.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.moco_g14_me_wa_os.ui.theme.Moco_G14_Me_Wa_OsTheme
//import com.example.moco_g14_me_wa_os.Todo.TodoApp
import dagger.hilt.android.AndroidEntryPoint


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import com.example.moco_g14_me_wa_os.Timer.TimerViewModelFactory


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Apply your app theme
            Moco_G14_Me_Wa_OsTheme {
                // Render the TodoApp composable, passing in the ViewModel
                MainNavigation()            }
        }
    }
}
