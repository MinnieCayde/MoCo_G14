package com.example.moco_g14_me_wa_os.Settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()){
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val selectedWorkBreakOption by viewModel.selectedWorkBreak.collectAsState()
    val selectedSessionBreakOption by viewModel.selectedSessionBreak.collectAsState()
    val sessionCount by viewModel.sessionCountValue.collectAsState()


    val colors = MaterialTheme.colorScheme

    var textValue by remember { mutableStateOf(sessionCount.toString()) }
    var errorState by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .padding(8.dp)
        .padding(top = 24.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
        Row (modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Text("Settings",
                style = TextStyle(fontSize = 24.sp),
                fontWeight = FontWeight.ExtraBold,
                color = colors.onPrimary)
        }
        // Dark Mode Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode",
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 16.sp),
                color = colors.onPrimary)
            Switch(
                checked = isDarkMode,
                onCheckedChange = {viewModel.toggleDarkmode(it)},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.secondary,     // Farbe des Knopfs, wenn aktiviert
                    uncheckedThumbColor = colors.onPrimary,       // Farbe des Knopfs, wenn deaktiviert
                    checkedTrackColor = colors.primary,        // Farbe des Tracks, wenn aktiviert
                    uncheckedTrackColor = colors.primary,
                    uncheckedBorderColor = colors.onPrimary,      // **Border color when unchecked**
                    checkedBorderColor = colors.secondary
                )
            )
        }

        // Notification Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Notifications",
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontSize = 16.sp),
                color = colors.onPrimary)
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = {viewModel.toggleNotifications(it)},
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.secondary,       // Farbe des Knopfs, wenn aktiviert
                    uncheckedThumbColor = colors.onPrimary,       // Farbe des Knopfs, wenn deaktiviert
                    checkedTrackColor = colors.primary,        // Farbe des Tracks, wenn aktiviert
                    uncheckedTrackColor = colors.primary,
                    uncheckedBorderColor = colors.onPrimary,      // **Border color when unchecked**
                    checkedBorderColor = colors.secondary
                )
            )
        }

        // WorkBreak
        Text("Work Break Duration",
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(bottom = 8.dp),
            style = TextStyle(fontSize = 16.sp),
            fontWeight = FontWeight.Bold,
            color = colors.onPrimary)

        val workBreakOptions = listOf(5,10) // Timer Optionen in Minuten
        workBreakOptions.forEach { option ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$option minutes",
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onPrimary)
                RadioButton(
                    selected = (selectedWorkBreakOption == option),
                    onClick = {viewModel.setWorkBreakOption(option)},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colors.secondary,        // Farbe, wenn ausgewählt
                        unselectedColor = colors.onPrimary,
                        )
                )
            }
        }

        // SessionBreak
        Text("Session Break Duration",
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(bottom = 8.dp),
            style = TextStyle(fontSize = 16.sp),
            fontWeight = FontWeight.Bold,
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
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onPrimary)
                RadioButton(
                    selected = (selectedSessionBreakOption == option),
                    onClick = {viewModel.setSessioBreakOption(option)},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colors.secondary,
                        unselectedColor = colors.onPrimary,
                    )
                )
            }
        }
        // Eingabefeld Sessions
        Text(text = "Sessionanzahl:",
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(bottom = 8.dp),
            color = colors.onPrimary,
            style = TextStyle(fontSize = 16.sp),
        )
        TextField(
            value = textValue,
            onValueChange = { newValue ->
                textValue = newValue // Lokalen Zustand updaten

                val newCount = newValue.toIntOrNull()

                // Wenn der neue Wert eine gültige Zahl ist, wird der Fehlerzustand entfernt
                if (newCount != null && newCount >= 0) {
                    errorState = false
                    viewModel.setSessionCount(newCount)  // Gültige Zahl wird im ViewModel gesetzt
                } else if (newValue.isEmpty()) {
                    // Wenn das Textfeld leer ist, setzen wir keinen neuen Wert, sondern erlauben die leere Eingabe
                    errorState = false
                } else {
                    // Ungültige Eingabe wird als Fehler betrachtet
                    errorState = true
                }
            },
            label = { Text("Input") },
            modifier = Modifier.fillMaxWidth(),
        )
        // Optionaler Fehlertext
        if (errorState) {
            Text(
                text = "Bitte eine gültige Zahl eingeben",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

// Vorschau der SettingsScreen mit Mock-Daten
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview(viewModel: SettingsViewModel = hiltViewModel()) {
    // Mock-Implementierung, um die Vorschau zu ermöglichen
    val isDarkMode = remember { mutableStateOf(false) }
    val notificationsEnabled = remember { mutableStateOf(true) }
    val selectedWorkTimerOption = remember { mutableStateOf(25) }
    val colors = MaterialTheme.colorScheme
    val sessionCount by viewModel.sessionCountValue.collectAsState()

    Column(modifier = Modifier.padding(8.dp)) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text("Settings",
            style = TextStyle(fontSize = 24.sp), fontWeight = FontWeight.SemiBold, color = colors.onPrimary)
        }
        // Dark Mode Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Dark Mode")
            Switch(
                checked = isDarkMode.value,
                onCheckedChange = { viewModel.toggleDarkmode(it)}
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
                onCheckedChange = { viewModel.toggleNotifications(it)}
            )
        }

        // WorkBreak
        Text(text = "Work Break Duration", modifier = Modifier
            .padding(top = 16.dp)
            .padding(bottom = 8.dp),
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
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(bottom = 8.dp),
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
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(bottom = 8.dp),
            color = colors.onPrimary
        )
        TextField(
            value = sessionCount.toString(),
            onValueChange = { newValue ->
                val newCount = newValue.toIntOrNull()

                if (newCount != null && newCount != sessionCount){
                    viewModel.setSessionCount(newCount)
                }
            },
            label = { Text("Input") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
