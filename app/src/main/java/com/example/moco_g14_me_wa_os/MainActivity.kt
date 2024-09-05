package com.example.moco_g14_me_wa_os

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import android.graphics.Color
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.floatIntMapOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moco_g14_me_wa_os.ui.theme.Moco_G14_Me_Wa_OsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)



        // Referenzen zu den ToggleButtons (3 ersten sind für Session Selection)
        val toggleOption1 = findViewById<ToggleButton>(R.id.toggleOption1)
        val toggleOption2 = findViewById<ToggleButton>(R.id.toggleOption2)
        val toggleOption3 = findViewById<ToggleButton>(R.id.toggleOption3)
        val worktimeOption1 = findViewById<ToggleButton>(R.id.workTimeOption1)
        val worktimeOption2 = findViewById<ToggleButton>(R.id.workTimeOption2)
        val shortBreakTimeOption1 = findViewById<ToggleButton>(R.id.shortBreakTimeOption1)
        val shortBreakTimeOption2 = findViewById<ToggleButton>(R.id.shortBreakTimeOption2)
        val longBreakTimeOption1 = findViewById<ToggleButton>(R.id.longBreakTimeOption1)
        val longBreakTimeOption2 = findViewById<ToggleButton>(R.id.longBreakTimeOption2)

        // Alle ToggleButtons in eine Liste aufnehmen
        val toggleButtons = listOf(toggleOption1, toggleOption2, toggleOption3)

        // Event Listener für jeden ToggleButton
        for (toggle in toggleButtons) {
            toggle.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Nur dieser ToggleButton wird aktiv - alle anderen deaktivieren
                    deactivateOthers(toggleButtons, buttonView.id)
                    buttonView.setBackgroundResource(R.drawable.active_rounded_time_selectors)
                    buttonView.setTextColor(resources.getColor(R.color.white))
                } else {
                    buttonView.setBackgroundResource(R.drawable.inactive_rounded_time_selectors)
                }
            }
        }
    }

    private fun deactivateOthers(toggleButtons: List<ToggleButton>, activeid: Int) {
        for (toggle in toggleButtons) {
            if (toggle.id != activeid) {
                toggle.isChecked = false
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Moco_G14_Me_Wa_OsTheme {
        Greeting("Android")
    }
}