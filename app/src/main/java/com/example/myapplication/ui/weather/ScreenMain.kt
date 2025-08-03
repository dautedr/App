package com.example.myapplication.ui.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.data.model.ForecastDay
import com.example.myapplication.ui.common.crossfadeContent
import com.example.myapplication.ui.common.dynamicSkyBrush
import com.example.myapplication.ui.common.glassCardModifier
import com.example.myapplication.ui.theme.PurpleInk
import com.example.myapplication.ui.theme.SurfaceVariant
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.ui.theme.TextTertiary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ScreenMain(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Try to ensure we have data on first enter
    LaunchedEffect(Unit) {
        // no-op here; MainActivity already triggers fetch via permissions
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    // Dynamic gradient based on current condition; fallback to pleasant default
                    when (val state = uiState) {
                        is WeatherUiState.Success -> {
                            val cond = state.weather.current.condition.text
                            val isNight = state.weather.current.isDay == 0
                            dynamicSkyBrush(cond, isNight)
                        }
                        else -> Brush.linearGradient(listOf(Color(0xFFD6E6F2), Color(0xFFF5E9F0)))
                    }
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    LoadingSection()
                }
                is WeatherUiState.Error -> {
                    ErrorSection(
                        message = state.message,
                        onRetry = { viewModel.fetchWeatherForCurrentLocation() }
                    )
                }
                is WeatherUiState.Success -> {
                    val weather = state.weather
                    val forecastDays = weather.forecast?.forecastday.orEmpty().take(3)
                    crossfadeContent(targetState = weather) { _ ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                TitleApp()
                            }
                            item {
                                CurrentWeatherHeader(
                                    dateText = formatDateSpanish(),
                                    tempC = weather.current.tempC,
                                    conditionText = weather.current.condition.text,
                                    iconUrl = "https:${weather.current.condition.icon}",
                                    location = "${weather.location.name}, ${weather.location.country}"
                                )
                            }
                            item {
                                ForecastChipsWithSparkline(forecastDays)
                            }
                            item {
                                FooterLabel()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleApp() {
    Text(
        text = "Minimal Weather",
        style = MaterialTheme.typography.titleLarge.copy(
            color = PurpleInk,
            fontWeight = FontWeight.W300
        ),
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LoadingSection() {
    // Simple center loading + shimmer-like card placeholders
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Cargando el tiempo...",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6B7280))
        )
    }
}

@Composable
private fun ErrorSection(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Algo salió mal",
            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFB91C1C))
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6B7280)),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Reintentar")
        }
    }
}

@Composable
private fun CurrentWeatherHeader(
    dateText: String,
    tempC: Double,
    conditionText: String,
    iconUrl: String,
    location: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .glassCardModifier(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF).copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
            Spacer(Modifier.height(8.dp))

            // Temperature over icon with blurred halo
            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                // Halo
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant.copy(alpha = 0.55f))
                        .blur(24.dp)
                )
                // Condition icon
                AsyncImage(
                    model = iconUrl,
                    contentDescription = conditionText,
                    modifier = Modifier.size(110.dp),
                )
                // Big temperature
                Text(
                    text = "${tempC.toInt()}°",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Color(0xFF374151),
                        fontWeight = FontWeight.W200
                    )
                )
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = conditionText,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PurpleInk,
                    fontWeight = FontWeight.W300
                )
            )
            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextTertiary),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ForecastChipsWithSparkline(days: List<ForecastDay>) {
    if (days.isEmpty()) return
    // Clamp to 3 items
    val items = days.take(3)
    val selectedIndex = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pronóstico a 3 Días",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PurpleInk,
                    fontWeight = FontWeight.W600
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            // Chips row
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items.forEachIndexed { index, day ->
                    val isSelected = selectedIndex.value == index
                    androidx.compose.material3.FilterChip(
                        selected = isSelected,
                        onClick = { selectedIndex.value = index },
                        label = {
                            Text(
                                text = formatDayNameSpanish(day.date),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W500
                                )
                            )
                        },
                        leadingIcon = {
                            AsyncImage(
                                model = "https:${day.day.condition.icon}",
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Sparkline for highs (simple, across the 3 items)
            SparklineHighsLows(items)

            Spacer(Modifier.height(16.dp))

            // Crossfade details of selected
            crossfadeContent(selectedIndex.value) { idx ->
                val d = items[idx]
                ForecastRowSimple(
                    dayName = formatDayNameSpanish(d.date),
                    iconUrl = "https:${d.day.condition.icon}",
                    high = d.day.maxtemp_c,
                    low = d.day.mintemp_c
                )
            }
        }
    }
}

@Composable
private fun SparklineHighsLows(items: List<ForecastDay>) {
    // Draw simple line for highs; compute normalized points
    val highs = items.map { it.day.maxtemp_c }
    val lows = items.map { it.day.mintemp_c }
    val max = (highs.maxOrNull() ?: 0.0).coerceAtLeast(1.0)
    val min = (lows.minOrNull() ?: 0.0)

    val points = highs.mapIndexed { index, v ->
        val x = index / (highs.lastIndex.takeIf { it > 0 }?.toFloat() ?: 1f)
        val y = if (max == min) 0.5f else 1f - ((v - min) / (max - min)).toFloat()
        x to y
    }

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFFFFFFF).copy(alpha = 0.35f))
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        val w = size.width
        val h = size.height
        // Path line with smoother curves
        if (points.size > 1) {
            val path = androidx.compose.ui.graphics.Path()
            val adjustedPoints = points.map { (x, y) -> androidx.compose.ui.geometry.Offset(x * w, y * h) }
            
            path.moveTo(adjustedPoints[0].x, adjustedPoints[0].y)
            for (i in 1 until adjustedPoints.size) {
                path.lineTo(adjustedPoints[i].x, adjustedPoints[i].y)
            }
            
            drawPath(
                path = path,
                color = Color(0xFF69A6D6),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 8f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
        
        // Knobs with better styling
        points.forEach { (x, y) ->
            val center = androidx.compose.ui.geometry.Offset(x * w, y * h)
            // Outer glow
            drawCircle(
                color = Color(0xFF7AAECB).copy(alpha = 0.3f),
                radius = 16f,
                center = center
            )
            // Main knob
            drawCircle(
                color = Color(0xFF7AAECB),
                radius = 10f,
                center = center
            )
            // Inner highlight
            drawCircle(
                color = Color.White,
                radius = 4f,
                center = center
            )
        }
    }
}

@Composable
private fun ForecastRowSimple(dayName: String, iconUrl: String, high: Double, low: Double) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(Color(0xFFFFFFFF).copy(alpha = 0.35f))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = PurpleInk,
                fontWeight = FontWeight.W500
            )
        )
        AsyncImage(
            model = iconUrl,
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${high.toInt()}°",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color(0xFF374151),
                    fontWeight = FontWeight.W600
                )
            )
            Spacer(Modifier.height(0.dp)) // spacer placeholder
            Text(
                text = "  ${low.toInt()}°",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF8C8CA1))
            )
        }
    }
}

@Composable
private fun FooterLabel() {
            Text(
                text = "🔄 LIVE DATA - FORECAST",
                style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF00AEEF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
}

private fun formatDateSpanish(): String {
    val today = LocalDate.now()
    val dayName = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es"))
    val monthName = today.month.getDisplayName(TextStyle.FULL, Locale("es"))
    return "${dayName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es")) else it.toString() }}, ${today.dayOfMonth} de ${monthName} de ${today.year}"
}

private fun formatDayNameSpanish(dateStr: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateStr, formatter)
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale("es")) else it.toString()
        }
    } catch (_: Exception) {
        dateStr
    }
}
