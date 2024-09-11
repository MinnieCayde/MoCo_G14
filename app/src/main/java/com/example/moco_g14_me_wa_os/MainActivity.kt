package com.example.moco_g14_me_wa_os
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.moco_g14_me_wa_os.ui.theme.Moco_G14_Me_Wa_OsTheme
//import com.example.moco_g14_me_wa_os.Todo.TodoApp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Apply your app theme
            Moco_G14_Me_Wa_OsTheme {
                // Render the TodoApp composable, passing in the ViewModel
                MainNavigation()            }
        }
    }
}
