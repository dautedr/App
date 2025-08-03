package com.example.myapplication.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.example.myapplication.ui.theme.PurpleInk
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import com.example.myapplication.ui.theme.TextTertiary

@Composable
fun ScreenSettings() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFFD6E6F2), Color(0xFFF5E9F0))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Minimal Weather",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = PurpleInk,
                    fontWeight = FontWeight.W300
                ),
                modifier = Modifier.fillMaxWidth()
            )

            SettingsGroup(title = "Unidades") {
                SettingValueRow(label = "Temperatura", value = "Celsius (°C)")
                SettingValueRow(label = "Velocidad del Viento", value = "km/h")
                SettingValueRow(label = "Presión", value = "hPa")
            }

            val gpsOn = remember { mutableStateOf(true) }
            SettingsGroup(title = "Ubicación") {
                SettingValueRow(label = "Ubicación Actual", value = "Automática")
                SettingToggleRow(label = "GPS Automático", checked = gpsOn.value) { gpsOn.value = it }
            }

            val alertsOn = remember { mutableStateOf(true) }
            val dailyOn = remember { mutableStateOf(false) }
            val rainOn = remember { mutableStateOf(true) }
            SettingsGroup(title = "Notificaciones") {
                SettingToggleRow(label = "Alertas Meteorológicas", checked = alertsOn.value) { alertsOn.value = it }
                SettingToggleRow(label = "Pronóstico Diario", checked = dailyOn.value) { dailyOn.value = it }
                SettingToggleRow(label = "Lluvia Próxima", checked = rainOn.value) { rainOn.value = it }
            }

            val iconsAnimated = remember { mutableStateOf(false) }
            SettingsGroup(title = "Apariencia") {
                SettingValueRow(label = "Tema", value = "Automático")
                SettingToggleRow(label = "Iconos Animados", checked = iconsAnimated.value) { iconsAnimated.value = it }
            }
        }
    }
}

@Composable
private fun SettingsGroup(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = PurpleInk,
                fontWeight = FontWeight.W600
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SettingValueRow(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF).copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.W500
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextTertiary,
                    fontWeight = FontWeight.W500
                )
            )
        }
    }
}

@Composable
private fun SettingToggleRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (checked) Color(0xFFFFFFFF).copy(alpha = 0.8f) else Color(0xFFFFFFFF).copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = if (checked) PurpleInk else TextPrimary,
                    fontWeight = FontWeight.W500
                )
            )
            Switch(
                checked = checked, 
                onCheckedChange = onCheckedChange,
                thumbContent = if (checked) {
                    @Composable {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                } else null
            )
        }
    }
}
