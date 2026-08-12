package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BentoLavenderPrimary,
    onPrimary = BentoDeepPurple,
    primaryContainer = BentoDeepPurple,
    onPrimaryContainer = BentoLightPurple,
    secondary = BentoLightPurple,
    onSecondary = BentoDeepPurple,
    tertiary = BentoMediumPurple,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BentoBorder,
    error = BentoFlameCoral,
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = CyanPrimaryVariant,
    onPrimary = Color.White,
    secondary = EmeraldSecondary,
    tertiary = PurpleAccent,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun HscMentorTheme(
    darkTheme: Boolean = true, // Default to dark theme as requested
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

