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
import com.example.moco_g14_me_wa_os.ui.theme.Moco_G14_Me_Wa_OsTheme

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()){
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val selectedWorkBreakOption by viewModel.selectedWorkBreak.collectAsState()
    val selectedSessionBreakOption by viewModel.selectedSessionBreak.collectAsState()
    val colors = MaterialTheme.colorScheme


    Column(modifier = Modifier.padding(8.dp).padding(top = 24.dp).fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Text("Settings",
                style = TextStyle(fontSize = 24.sp),
                fontWeight = FontWeight.SemiBold,
                color = colors.onPrimary)
        }
        // Dark Mode Switch
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode",
                color = colors.onPrimary)
            Switch(
                checked = isDarkMode,
                onCheckedChange = {viewModel.toggleDarkmode(it)}
            )
        }

        // Notification Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Notifications",
                color = colors.onPrimary)
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {viewModel.toggleDarkmode(it) }
            )
        }

        // WorkBreak
        Text("Work Break Duration",
            modifier = Modifier.padding(top = 16.dp).padding(bottom = 8.dp),
            style = TextStyle(),
            fontWeight = FontWeight.SemiBold,
            color = colors.onPrimary)

        val workBreakOptions = listOf(5,10) // Timer Optionen in Minuten
        workBreakOptions.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$option minutes",
                    color = colors.onPrimary)
                RadioButton(
                    selected = (selectedWorkBreakOption == option),
                    onClick = {viewModel.setWorkBreakOption(option)}
                )
            }
        }

        // SessionBreak
        Text("Session Break Duration",
            modifier = Modifier.padding(top = 16.dp).padding(bottom = 8.dp),
            style = TextStyle(),
            fontWeight = FontWeight.SemiBold,
            color = colors.onPrimary)

        val sessionBreakOptions = listOf(15,30) // Timer Optionen in Minuten
        sessionBreakOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$option minutes",
                    color = colors.onPrimary)
                RadioButton(
                    selected = (selectedSessionBreakOption == option), // noch andere states im Repository machen, geht aktuell alles über worktimer
                    onClick = {viewModel.setSessioBreakOption(option)}
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
    val colors = MaterialTheme.colorScheme
    val textFieldValue = remember { mutableStateOf("1") } // Eingabefeld initialisiert mit "1"

    Column(modifier = Modifier.padding(8.dp)) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("Settings",
            style = TextStyle(fontSize = 24.sp), fontWeight = FontWeight.SemiBold, color = colors.onPrimary)
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
        Text(text = "Work Break Duration", modifier = Modifier.padding(top = 16.dp).padding(bottom = 8.dp),
            style = TextStyle(),
            fontWeight = FontWeight.SemiBold)

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
        Text(text = "Session Break Duration",
            modifier = Modifier.padding(top = 16.dp).padding(bottom = 8.dp),
            style = TextStyle(),
            fontWeight = FontWeight.SemiBold)

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

        // Eingabefeld hinzufügen
        Text(text = "Sessionanzahl:",
            modifier = Modifier.padding(top = 16.dp).padding(bottom = 8.dp)
        )
        TextField(
            value = textFieldValue.value,
            onValueChange = { textFieldValue.value = it },
            label = { Text("Input") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
