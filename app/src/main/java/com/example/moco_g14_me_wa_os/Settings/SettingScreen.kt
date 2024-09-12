package com.example.moco_g14_me_wa_os.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()){
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

// Vorschau der SettingsScreen mit Mock-Daten
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    // Mock-Implementierung, um die Vorschau zu ermöglichen
    val isDarkMode = remember { mutableStateOf(false) }
    val notificationsEnabled = remember { mutableStateOf(true) }
    val selectedWorkTimerOption = remember { mutableStateOf(25) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("Settings",
            style = TextStyle(fontSize = 24.sp), fontWeight = FontWeight.SemiBold, )
        }
        // Dark Mode Switch
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode")
            Switch(
                checked = isDarkMode.value,
                onCheckedChange = { isDarkMode.value = it }
            )
        }

        // Notification Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Notifications")
            Switch(
                checked = notificationsEnabled.value,
                onCheckedChange = { notificationsEnabled.value = it }
            )
        }

        // WorkBreak
        Text(text = "Work Break Duration", modifier = Modifier.padding(top = 16.dp))
        val workBreakOptions = listOf(5,10) // Timer Optionen in Minuten
        workBreakOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$option minutes")
                RadioButton(
                    selected = (selectedWorkTimerOption.value == option),
                    onClick = { selectedWorkTimerOption.value = option }
                )
            }
        }

        // SessionBreak
        Text(text = "Session Break Duration", modifier = Modifier.padding(top = 16.dp))
        val sessionBreakOptions = listOf(15,30) // Timer Optionen in Minuten
        sessionBreakOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$option minutes")
                RadioButton(
                    selected = (selectedWorkTimerOption.value == option),
                    onClick = { selectedWorkTimerOption.value = option }
                )
            }
        }
    }
}
