package com.example.myapplication.ui.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

// Dynamic sky gradients based on condition and time
fun dynamicSkyBrush(conditionText: String?, isNight: Boolean): Brush {
    val c = conditionText?.lowercase() ?: ""
    return when {
        isNight && ("clear" in c || "sunny" in c) -> Brush.linearGradient(
            listOf(Color(0xFF0D1B2A), Color(0xFF1B263B), Color(0xFF415A77))
        )
        isNight -> Brush.linearGradient(
            listOf(Color(0xFF0A0F1E), Color(0xFF151B2D), Color(0xFF222E45))
        )
        "rain" in c || "shower" in c || "lluv" in c -> Brush.linearGradient(
            listOf(Color(0xFF7AAECB), Color(0xFFA6C8DD), Color(0xFFD3E3EF))
        )
        "cloud" in c || "nublado" in c -> Brush.linearGradient(
            listOf(Color(0xFFC8D3E3), Color(0xFFDDE3EF), Color(0xFFF2F3F8))
        )
        "snow" in c || "nieve" in c -> Brush.linearGradient(
            listOf(Color(0xFFE6F3FF), Color(0xFFF3F9FF), Color(0xFFFFFFFF))
        )
        else -> Brush.linearGradient( // sunny / default
            listOf(Color(0xFFD6E6F2), Color(0xFFF5E9F0))
        )
    }
}

// Glassmorphism card helper
fun Modifier.glassCardModifier(): Modifier = this
    .clip(RoundedCornerShape(24.dp))
    .background(Color(0xFFFFFFFF).copy(alpha = 0.45f))
    .graphicsLayer {
        shadowElevation = 12f
        shape = RoundedCornerShape(24.dp)
        clip = true
    }

// Simple shimmer placeholder using animated brush (lightweight)
fun shimmerBrush(timeMs: Long): Brush {
    val phase = ((timeMs % 1200).toFloat() / 1200f)
    val base = 0.8f + 0.2f * kotlin.math.sin(phase * 2 * Math.PI).toFloat().absoluteValue
    val c1 = Color(0xFFF2F3F8).copy(alpha = 0.7f * base)
    val c2 = Color(0xFFFFFFFF).copy(alpha = 0.7f)
    return Brush.linearGradient(listOf(c1, c2, c1))
}

fun Modifier.shimmerPlaceholder(timeMs: Long): Modifier = this
    .fillMaxWidth()
    .clip(RoundedCornerShape(20.dp))
    .background(shimmerBrush(timeMs))

// Crossfade helper
@Composable
fun <T> crossfadeContent(targetState: T, content: @Composable (T) -> Unit) {
    Crossfade(targetState = targetState, label = "content-crossfade") { state ->
        content(state)
    }
}
