package com.example.myapplication.data.repository

import com.example.myapplication.data.model.WeatherResponse
import com.example.myapplication.data.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService
) {
    suspend fun getForecastWeather(location: String, days: Int = 3): WeatherResponse {
        return apiService.getForecastWeather(location = location, days = days)
    }
}
