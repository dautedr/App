package com.example.myapplication.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.WeatherResponse
import com.example.myapplication.data.repository.WeatherRepository
import com.example.myapplication.location.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        fetchWeatherForCurrentLocation()
    }

    fun fetchWeatherForCurrentLocation(days: Int = 3) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val location = locationTracker.getCurrentLocation()
                if (location != null) {
                    val locationString = "${location.latitude},${location.longitude}"
                    val weather = weatherRepository.getForecastWeather(locationString, days)
                    _uiState.value = WeatherUiState.Success(weather)
                } else {
                    // Fallback to default location if location is not available
                    fetchWeather("Las Palmas de Gran Canaria", days)
                }
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun fetchWeather(location: String, days: Int = 3) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val weather = weatherRepository.getForecastWeather(location, days)
                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
