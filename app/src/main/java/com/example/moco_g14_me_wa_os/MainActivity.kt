package com.example.moco_g14_me_wa_os

import android.os.Bundle
import android.widget.ToggleButton
import androidx.activity.ComponentActivity
import android.graphics.Color
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.floatIntMapOf
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moco_g14_me_wa_os.ui.theme.Moco_G14_Me_Wa_OsTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)

        /*
        // Referenzen zu den ToggleButtons
        val toggleOption1 = findViewById<ToggleButton>(R.id.toggleOption1)
        val toggleOption2 = findViewById<ToggleButton>(R.id.toggleOption2)
        val toggleOption3 = findViewById<ToggleButton>(R.id.toggleOption3)

        // Alle ToggleButtons in eine Liste aufnehmen
        val toggleButtons = listOf(toggleOption1, toggleOption2, toggleOption3)

        // Event Listener für jeden ToggleButton
        for (toggle in toggleButtons) {
            toggle.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Nur dieser ToggleButton wird aktiv - alle anderen deaktivieren
                    deactivateOthers(toggleButtons, buttonView.id)

                    // Aktives Drawable setzen
                    buttonView.setBackgroundResource(R.drawable.active_rounded_time_selectors)

                    // Schriftfarbe ändern (optional)
                    buttonView.setTextColor(resources.getColor(R.color.white))
                } else {
                    // Inaktives Drawable setzen
                    buttonView.setBackgroundResource(R.drawable.inactive_rounded_time_selectors)
                }
            }
        }
    }

    // Methode um alle anderen ToggleButtons zu deaktivieren
    private fun deactivateOthers(toggleButtons: List<ToggleButton>, activeId: Int) {
        for (toggle in toggleButtons) {
            if (toggle.id != activeId) {
                toggle.isChecked = false
            }
        }
    }
    */
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
}
@Serializable


