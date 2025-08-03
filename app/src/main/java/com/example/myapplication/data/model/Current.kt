package com.example.myapplication.data.model

import com.squareup.moshi.Json

data class Current(
    @Json(name = "last_updated_epoch") val lastUpdatedEpoch: Long,
    @Json(name = "last_updated") val lastUpdated: String,
    @Json(name = "temp_c") val tempC: Double,
    @Json(name = "temp_f") val tempF: Double,
    @Json(name = "is_day") val isDay: Int,
    val condition: Condition,
    @Json(name = "wind_mph") val windMph: Double,
    @Json(name = "wind_kph") val windKph: Double,
    @Json(name = "wind_degree") val windDegree: Int,
    @Json(name = "wind_dir") val windDir: String,
    @Json(name = "pressure_mb") val pressureMb: Double,
    @Json(name = "pressure_in") val pressureIn: Double,
    @Json(name = "precip_mm") val precipMm: Double,
    @Json(name = "precip_in") val precipIn: Double,
    val humidity: Int,
    val cloud: Int,
    @Json(name = "feelslike_c") val feelslikeC: Double,
    @Json(name = "feelslike_f") val feelslikeF: Double,
    @Json(name = "vis_km") val visKm: Double,
    @Json(name = "vis_miles") val visMiles: Double,
    val uv: Double,
    @Json(name = "gust_mph") val gustMph: Double,
    @Json(name = "gust_kph") val gustKph: Double
) 