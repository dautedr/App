package com.example.myapplication.data.network

import com.example.myapplication.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast.json")
    suspend fun getForecastWeather(
        @Query("q") location: String,
        @Query("days") days: Int = 3,
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): WeatherResponse
}
