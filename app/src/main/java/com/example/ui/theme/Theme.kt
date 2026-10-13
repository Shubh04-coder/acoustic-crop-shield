package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = DarkBackground,
    primaryContainer = LightForestGreen,
    onPrimaryContainer = DarkOnSurface,
    secondary = SproutGreen,
    onSecondary = SoftSurface,
    background = DarkBackground,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMuted
)

private val LightColorScheme = lightColorScheme(
    primary = DeepForestGreen,
    onPrimary = SoftSurface,
    primaryContainer = Color(0xFFD1E8D5),
    onPrimaryContainer = DeepForestGreen,
    secondary = SproutGreen,
    onSecondary = SoftSurface,
    background = EarthSand,
    onBackground = DarkText,
    surface = SoftSurface,
    onSurface = DarkText,
    surfaceVariant = Color(0xFFE8F0EA),
    onSurfaceVariant = MutedText
)

@Composable
fun CropShieldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
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
