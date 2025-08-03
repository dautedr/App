package com.example.myapplication.ui.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.layout.layoutId
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplication.data.model.Hour
import com.example.myapplication.ui.common.dynamicSkyBrush
import com.example.myapplication.ui.common.glassCardModifier
import com.example.myapplication.ui.theme.PurpleInk
import com.example.myapplication.ui.theme.SurfaceVariant
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.ui.theme.TextTertiary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

import android.graphics.Canvas

@Composable
fun ScreenDetails(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Cargando detalles...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6B7280))
                        )
                    }
                }
                is WeatherUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No se pudieron cargar los detalles",
                            style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFFB91C1C))
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF6B7280)),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                is WeatherUiState.Success -> {
                    val weather = state.weather
                    val forecastDays = weather.forecast?.forecastday.orEmpty()
                    val todayHours: List<Hour> = forecastDays.firstOrNull()?.hour.orEmpty()

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item { TitleApp() }

                        item {
                            CurrentHeader(
                                dateText = formatDateSpanish(),
                                tempC = weather.current.tempC,
                                conditionText = weather.current.condition.text,
                                iconUrl = "https:${weather.current.condition.icon}",
                                location = "${weather.location.name}, ${weather.location.country}"
                            )
                        }

                        if (todayHours.isNotEmpty()) {
                            item { HourlySection(todayHours) }
                        }

                        item {
                            DetailsGrid(
                                humidity = weather.current.humidity?.toDouble(),
                                uv = weather.current.uv?.toDouble(),
                                wind = weather.current.windKph,
                                windDir = weather.current.windDir,
                                pressure = weather.current.pressureMb?.toDouble(),
                                visibility = weather.current.visKm
                            )
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
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CurrentHeader(
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.45f)),
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
            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(SurfaceVariant.copy(alpha = 0.55f))
                        .blur(24.dp)
                )
                AsyncImage(
                    model = iconUrl,
                    contentDescription = conditionText,
                    modifier = Modifier.size(110.dp),
                )
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
private fun HourlySection(hours: List<Hour>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Pronóstico por Horas",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = PurpleInk,
                    fontWeight = FontWeight.W600
                )
            )
            Spacer(Modifier.height(8.dp))

            val choices = listOf("Próx. 6h", "Próx. 12h", "Todo")
            val selected = remember { mutableIntStateOf(0) }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                choices.forEachIndexed { idx, label ->
                    FilterChip(
                        selected = selected.intValue == idx,
                        onClick = { selected.intValue = idx },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            val filtered = when (selected.intValue) {
                0 -> hours.take(6)
                1 -> hours.take(12)
                else -> hours
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered.size) { idx ->
                    val h = filtered[idx]
                    HourPill(
                        timeLabel = formatHour(h.time),
                        iconUrl = "https:${h.condition.icon}",
                        temp = h.temp_c,
                        isNow = timeLabelIsNow(h.time)
                    )
                }
            }
        }
    }
}

private fun timeLabelIsNow(apiTime: String): Boolean {
    return try {
        val dt = LocalDateTime.parse(apiTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val now = LocalDateTime.now()
        dt.hour == now.hour && dt.toLocalDate() == now.toLocalDate()
    } catch (_: Exception) {
        false
    }
}

@Composable
private fun HourPill(timeLabel: String, iconUrl: String, temp: Double, isNow: Boolean) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isNow) Color(0xFFFFFFFF).copy(alpha = 0.8f) else Color(0xFFFFFFFF).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isNow) "Ahora" else timeLabel,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = if (isNow) PurpleInk else TextSecondary,
                    fontWeight = if (isNow) FontWeight.W700 else FontWeight.W500
                )
            )
            Spacer(Modifier.height(8.dp))
            AsyncImage(
                model = iconUrl,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${temp.roundToInt()}°",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.W600
                )
            )
        }
    }
}

@Composable
fun DetailsGrid(
    humidity: Double?,
    uv: Double?,
    wind: Double?,
    windDir: String?,
    pressure: Double?,
    visibility: Double?
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            DetailArcCard(
                title = "HUMEDAD",
                valueText = humidity?.toInt()?.let { "$it%" } ?: "—",
                progress = ((humidity ?: 0.0) / 100.0).toFloat().coerceIn(0f, 1f),
                color = Color(0xFF69A6D6)
            )
            DetailArcCard(
                title = "ÍNDICE UV",
                valueText = uv?.toInt()?.toString() ?: "—",
                progress = ((uv ?: 0.0) / 11.0).toFloat().coerceIn(0f, 1f),
                color = Color(0xFFEE7752)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.weight(1f)) {
                WindCompassCard(
                    wind = wind?.toFloat(),
                    windDir = windDir ?: "—"
                )
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "PRESIÓN",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.W600
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = pressure?.toInt()?.toString() ?: "—",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.W700
                        )
                    )
                    Text(
                        text = "hPa",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "VISIBILIDAD",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.W600
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = visibility?.toInt()?.let { "$it km" } ?: "—",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.W700
                        )
                    )
                }
            }
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.6f)),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "SENSACIÓN",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.W600
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.W700
                        )
                    )
                    Text(
                        text = "Como se siente",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailArcCard(
    title: String,
    valueText: String,
    progress: Float,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.W600
                )
            )
            Spacer(Modifier.height(12.dp))
            androidx.compose.foundation.Canvas(
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            ) {
                val stroke = 16f
                val start = -180f
                val sweep = 180f
                
                // Background arc with subtle shadow
                drawArc(
                    color = Color(0xFFE5E7EB).copy(alpha = 0.7f),
                    startAngle = start,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = stroke + 4f)
                )
                
                // Main background arc
                drawArc(
                    color = Color(0xFFE5E7EB),
                    startAngle = start,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                )
                
                // Progress arc with gradient effect
                drawArc(
                    color = color,
                    startAngle = start,
                    sweepAngle = sweep * progress,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
                )
                
                // Center dot
                drawCircle(
                    color = color,
                    radius = 6f,
                    center = center
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = valueText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.W700
                )
            )
        }
    }
}

@Composable
private fun WindCompassCard(wind: Float?, windDir: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "VIENTO",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.W600
                )
            )
            Spacer(Modifier.height(12.dp))
            androidx.compose.foundation.Canvas(
                modifier = Modifier.size(100.dp)
            ) {
                val center = center
                val radius = size.minDimension / 2.8f
                
                // Outer circle
                drawCircle(
                    color = Color(0xFFE5E7EB),
                    radius = radius,
                    center = center
                )
                
                // Inner circle
                drawCircle(
                    color = Color(0xFFFFFFFF),
                    radius = radius * 0.7f,
                    center = center
                )
                
                // Direction markers
                val directions = listOf("N", "E", "S", "W")
                val angles = listOf(-90f, 0f, 90f, 180f)

                // Draw direction letters using Compose text (avoids Android Canvas interop)
                directions.forEachIndexed { index, dir ->
                    val angle = Math.toRadians(angles[index].toDouble())
                    val tx = center.x + (radius * 0.85f) * kotlin.math.cos(angle).toFloat()
                    val ty = center.y + (radius * 0.85f) * kotlin.math.sin(angle).toFloat()

                    // Draw tick marks and placeholder dots for NSEW without using Android native canvas
                    // (Avoid nativeCanvas to keep compatibility across Compose versions)

                    // Draw tiny tick at each direction
                    val tickLen = 8f
                    val ix = center.x + (radius * 0.75f) * kotlin.math.cos(angle).toFloat()
                    val iy = center.y + (radius * 0.75f) * kotlin.math.sin(angle).toFloat()
                    val ox = center.x + (radius * 0.9f) * kotlin.math.cos(angle).toFloat()
                    val oy = center.y + (radius * 0.9f) * kotlin.math.sin(angle).toFloat()
                    drawLine(
                        color = Color(0xFF8C8CA1),
                        start = androidx.compose.ui.geometry.Offset(ix, iy),
                        end = androidx.compose.ui.geometry.Offset(ox, oy),
                        strokeWidth = 3f,
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )

                    // Draw a dot to mark where text would be
                    drawCircle(
                        color = Color(0xFF8C8CA1).copy(alpha = 0.6f),
                        radius = 3f,
                        center = androidx.compose.ui.geometry.Offset(tx, ty)
                    )
                }
                
                // Wind direction arrow
                val angle = when (windDir.firstOrNull()?.uppercaseChar()) {
                    'N' -> -90f
                    'E' -> 0f
                    'S' -> 90f
                    'W' -> 180f
                    else -> -90f
                }
                val rad = Math.toRadians(angle.toDouble())
                val x = center.x + radius * 0.6f * kotlin.math.cos(rad).toFloat()
                val y = center.y + radius * 0.6f * kotlin.math.sin(rad).toFloat()
                
                // Draw arrow line
                drawLine(
                    color = Color(0xFF69A6D6),
                    start = center,
                    end = androidx.compose.ui.geometry.Offset(x, y),
                    strokeWidth = 8f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                
                // Draw arrowhead
                val arrowAngle = Math.toRadians((angle - 180).toDouble())
                val arrowSize = 12f
                val arrowX1 = x + arrowSize * kotlin.math.cos(arrowAngle + Math.PI / 6).toFloat()
                val arrowY1 = y + arrowSize * kotlin.math.sin(arrowAngle + Math.PI / 6).toFloat()
                val arrowX2 = x + arrowSize * kotlin.math.cos(arrowAngle - Math.PI / 6).toFloat()
                val arrowY2 = y + arrowSize * kotlin.math.sin(arrowAngle - Math.PI / 6).toFloat()
                
                drawLine(
                    color = Color(0xFF69A6D6),
                    start = androidx.compose.ui.geometry.Offset(x, y),
                    end = androidx.compose.ui.geometry.Offset(arrowX1, arrowY1),
                    strokeWidth = 8f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF69A6D6),
                    start = androidx.compose.ui.geometry.Offset(x, y),
                    end = androidx.compose.ui.geometry.Offset(arrowX2, arrowY2),
                    strokeWidth = 8f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "${wind?.toInt() ?: 0} km/h",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.W700
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = windDir,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.W500
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun formatDateSpanish(): String {
    val today = LocalDate.now()
    val dayName = today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es"))
    val monthName = today.month.getDisplayName(TextStyle.FULL, Locale("es"))
    return "${dayName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es")) else it.toString() }}, ${today.dayOfMonth} de ${monthName} de ${today.year}"
}

private fun formatHour(apiTime: String): String {
    return try {
        val dt = LocalDateTime.parse(apiTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        dt.toLocalTime().toString().substring(0, 5)
    } catch (_: Exception) {
        apiTime
    }
}
