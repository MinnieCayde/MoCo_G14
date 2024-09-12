package com.example.moco_g14_me_wa_os.Settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = viewModel()){
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val selectedWorkTimerOptionn by viewModel.selectedWorkTimer.collectAsState()

    Column (modifier = Modifier.padding(10.dp)){
        //Dark Mode Switch
        Row(modifier = Modifier.fillMaxWidth().padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text("Dark Mode")
            Switch(
                checked = isDarkMode,
                onCheckedChange = {viewModel.toggleDarkmode(it)}
            )
        }

        //Notification Toggle
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text("Enable Notifications")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {viewModel.toggleNotifications(it)}
            )
        }

        //Timer Options
        Text(text = "Timer Duration", modifier = Modifier.padding(vertical = 8.dp))
        val workTimerOptions = listOf(25,45) //Timer Optionen in Minuten
        workTimerOptions.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(text = "$option minutes")
                RadioButton(
                    selected = (selectedWorkTimerOptionn == option),
                    onClick = { viewModel.setWorkTimerOption(option)}
                )

            }
        }
    }

}
