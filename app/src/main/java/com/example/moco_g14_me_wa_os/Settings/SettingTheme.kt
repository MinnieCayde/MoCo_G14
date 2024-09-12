package com.example.moco_g14_me_wa_os.Settings

import android.R.id.primary
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Typography
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable

// Beispiel für Typography
val Typography = Typography()

// Beispiel für Shapes
val Shapes = Shapes()


// Farben definieren
val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

private val DarkColorPalette = darkColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun SettingTheme(darkTheme: Boolean = false, content: @Composable () -> Unit){
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography, // Stelle sicher, dass du Typography definiert hast
        shapes = Shapes, // Stelle sicher, dass du Shapes definiert hast
        content = content
    )
}

