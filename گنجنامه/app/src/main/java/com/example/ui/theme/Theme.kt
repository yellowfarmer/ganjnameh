package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GanjnamehDarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = PersianAzureDark,
    primaryContainer = PersianAzureLight,
    onPrimaryContainer = GoldBright,
    secondary = TurquoisePrimary,
    onSecondary = Color.White,
    secondaryContainer = TurquoiseDark,
    onSecondaryContainer = TurquoiseLight,
    tertiary = GoldBright,
    onTertiary = PersianAzureDark,
    background = PersianAzureDark,
    onBackground = TextPrimaryDark,
    surface = PersianAzureSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = PersianAzure,
    onSurfaceVariant = TextSecondaryDark,
    error = RubyAccent,
    onError = Color.White
)

private val GanjnamehLightColorScheme = lightColorScheme(
    primary = PersianAzure,
    onPrimary = GoldBright,
    primaryContainer = ParchmentSurface,
    onPrimaryContainer = PersianAzureDark,
    secondary = TurquoiseDark,
    onSecondary = Color.White,
    secondaryContainer = TurquoiseLight.copy(alpha = 0.2f),
    onSecondaryContainer = TurquoiseDark,
    tertiary = GoldPrimary,
    onTertiary = Color.White,
    background = ParchmentBg,
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = ParchmentSurface,
    onSurfaceVariant = TextSecondaryLight,
    error = RubyAccent,
    onError = Color.White
)

@Composable
fun GanjnamehTheme(
    darkTheme: Boolean = true, // Default to royal Persian dark mode for rich atmosphere
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GanjnamehDarkColorScheme else GanjnamehLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    GanjnamehTheme(darkTheme = darkTheme, content = content)
}
