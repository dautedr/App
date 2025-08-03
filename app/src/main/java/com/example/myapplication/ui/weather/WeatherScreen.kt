package com.example.myapplication.ui.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.myapplication.data.model.WeatherResponse

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is WeatherUiState.Loading -> {
                LoadingIndicator()
            }
            is WeatherUiState.Success -> {
                WeatherSuccessView(weather = state.weather)
            }
            is WeatherUiState.Error -> {
                ErrorView(
                    message = state.message,
                    onRetry = { viewModel.fetchWeatherForCurrentLocation() }
                )
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun WeatherSuccessView(weather: WeatherResponse) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = weather.location.name,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "${weather.current.tempC}°C",
            style = MaterialTheme.typography.displayLarge
        )
        AsyncImage(
            model = "https:${weather.current.condition.icon}",
            contentDescription = weather.current.condition.text,
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = weather.current.condition.text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
} 