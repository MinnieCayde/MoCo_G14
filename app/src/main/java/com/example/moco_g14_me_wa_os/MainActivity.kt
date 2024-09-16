package com.example.moco_g14_me_wa_os


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moco_g14_me_wa_os.Settings.SettingsViewModel
import com.example.moco_g14_me_wa_os.ui.theme.Moco_G14_Me_Wa_OsTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: SettingsViewModel = hiltViewModel()
            val isDarkMode by viewModel.isDarkMode.collectAsState()

            // Use your main theme
            Moco_G14_Me_Wa_OsTheme(darkTheme = isDarkMode) {
                    MainNavigation()
            }
        }
    }
}
