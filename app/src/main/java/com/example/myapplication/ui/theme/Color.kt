package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

// Refined pastel palette with semantic roles
val PurpleInk = Color(0xFF5A4B6E)
val Lavender = Color(0xFFEDE7F6)
val Mist = Color(0xFFF2F3F8)
val Blush = Color(0xFFF5E9F0)
val Sky = Color(0xFFD6E6F2)
val Cloud = Color(0xFFF0EBF5)
val GlassSurface = Color(0xFFFFFFFF)

// Accents
val AccentSun = Color(0xFFFFC857)
val AccentRain = Color(0xFF7AAECB)
val AccentWind = Color(0xFF69A6D6)
val AccentUV = Color(0xFFEE7752)

// Wireframe-inspired colors
val TextPrimary = Color(0xFF4A5568)
val TextSecondary = Color(0xFF8C8CA1)
val TextTertiary = Color(0xFF7A7A8F)
val BackgroundGradientStart = Color(0xFFD6E6F2)
val BackgroundGradientEnd = Color(0xFFF5E9F0)
val CardBackground = Color(0xFFFFFFFF)
val CardStroke = Color(0xFFC8D3E3)

// Dark counterparts (for dynamic/night)
val InkDark = Color(0xFF1E1B26)
val NightSky1 = Color(0xFF0D1B2A)
val NightSky2 = Color(0xFF1B263B)
val NightSky3 = Color(0xFF415A77)

// Material color roles (fallback when dynamic color not available)
val Primary = PurpleInk
val OnPrimary = Color.White
val Secondary = AccentRain
val OnSecondary = Color.White
val Background = Mist
val OnBackground = TextPrimary
val Surface = GlassSurface
val OnSurface = TextPrimary
val SurfaceVariant = Cloud
val OnSurfaceVariant = TextSecondary
