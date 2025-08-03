package com.example.myapplication.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Shapes
import androidx.compose.foundation.shape.RoundedCornerShape

// Light/Dark fallback schemes (used when dynamic color is not available)
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = InkDark,
    onBackground = ColorSchemeDefaults.onDarkBackground,
    surface = InkDark,
    onSurface = ColorSchemeDefaults.onDarkSurface,
    surfaceVariant = NightSky2,
    onSurfaceVariant = ColorSchemeDefaults.onDarkSurface
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant
)

private object ColorSchemeDefaults {
    val onDarkBackground = OnSurface
    val onDarkSurface = OnSurface
}

// Rounded shapes consistent with wireframe
private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8),
    small = RoundedCornerShape(12),
    medium = RoundedCornerShape(20),
    large = RoundedCornerShape(24),
    extraLarge = RoundedCornerShape(28)
)

// Typography tweaks: rely on existing Typography.kt, ensure balanced weights at runtime.
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = AppShapes,
        typography = Typography,
        content = content
    )
}
