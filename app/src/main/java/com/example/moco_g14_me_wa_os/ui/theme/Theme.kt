package com.example.moco_g14_me_wa_os.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DodoAirDarkButtonBlue,
    onPrimary = DodopilotGray,
    secondary = DodopilotYellow,
    onSecondary = DodopilotWhite,
    background = DodoAirDarkBlue,
    surface = DodopilotDarkBlue,
    onSurface = DodopilotWhite,
    error = DodopilotYellow,
    onError = DodopilotGray
)

private val LightColorScheme = lightColorScheme(
    primary = DodoAirLightButtonBlue,
    onPrimary = DodoAirLightBlack,
    secondary = DodoAirYellow,
    onSecondary = DodoAirYellow,
    background = DodoAirLightBlue,
    surface = DodoAirYellow,
    onSurface = DodopilotDarkBlue,
    error = DodopilotYellow,
    onError = DodoAirDarkWhite
)

@Composable
fun Moco_G14_Me_Wa_OsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

