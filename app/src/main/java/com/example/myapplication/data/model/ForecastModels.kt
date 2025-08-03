package com.example.myapplication.data.model

data class Forecast(
    val forecastday: List<ForecastDay> = emptyList()
)

data class ForecastDay(
    val date: String,
    val day: Day,
    val astro: Astro? = null,
    val hour: List<Hour> = emptyList()
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avgtemp_c: Double? = null,
    val maxwind_kph: Double? = null,
    val totalprecip_mm: Double? = null,
    val avgvis_km: Double? = null,
    val avghumidity: Double? = null,
    val daily_will_it_rain: Int? = null,
    val daily_chance_of_rain: Int? = null,
    val uv: Double? = null,
    val condition: Condition
)

data class Astro(
    val sunrise: String? = null,
    val sunset: String? = null,
    val moonrise: String? = null,
    val moonset: String? = null,
    val moon_phase: String? = null,
    val moon_illumination: String? = null
)

data class Hour(
    val time: String,
    val temp_c: Double,
    val is_day: Int? = null,
    val wind_kph: Double? = null,
    val wind_dir: String? = null,
    val pressure_mb: Double? = null,
    val precip_mm: Double? = null,
    val humidity: Int? = null,
    val cloud: Int? = null,
    val feelslike_c: Double? = null,
    val windchill_c: Double? = null,
    val heatindex_c: Double? = null,
    val dewpoint_c: Double? = null,
    val will_it_rain: Int? = null,
    val chance_of_rain: Int? = null,
    val will_it_snow: Int? = null,
    val chance_of_snow: Int? = null,
    val vis_km: Double? = null,
    val gust_kph: Double? = null,
    val uv: Double? = null,
    val condition: Condition
)
