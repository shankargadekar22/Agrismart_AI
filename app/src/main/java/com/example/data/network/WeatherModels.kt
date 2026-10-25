package com.example.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenMeteoResponse(
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "timezone") val timezone: String?,
    @Json(name = "current") val current: CurrentWeatherDto?,
    @Json(name = "daily") val daily: DailyWeatherDto?
)

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    @Json(name = "time") val time: String?,
    @Json(name = "temperature_2m") val temperature: Double?,
    @Json(name = "relative_humidity_2m") val relativeHumidity: Double?,
    @Json(name = "precipitation") val precipitation: Double?,
    @Json(name = "weather_code") val weatherCode: Int?,
    @Json(name = "wind_speed_10m") val windSpeed: Double?
)

@JsonClass(generateAdapter = true)
data class DailyWeatherDto(
    @Json(name = "time") val time: List<String>?,
    @Json(name = "weather_code") val weatherCode: List<Int>?,
    @Json(name = "temperature_2m_max") val tempMax: List<Double>?,
    @Json(name = "temperature_2m_min") val tempMin: List<Double>?,
    @Json(name = "precipitation_probability_max") val rainProbability: List<Int>?
)

data class AgroWeatherDisplay(
    val locationName: String,
    val temperatureC: Double,
    val humidityPercent: Int,
    val precipitationMm: Double,
    val windSpeedKmh: Double,
    val weatherCondition: String,
    val weatherIconCode: Int,
    val sprayAdvisory: SprayStatus,
    val irrigationAdvisory: String,
    val fungalRiskAlert: String,
    val forecastDays: List<DailyForecastItem>,
    val isOfflineMode: Boolean
)

data class SprayStatus(
    val isSafe: Boolean,
    val title: String,
    val message: String
)

data class DailyForecastItem(
    val dayName: String,
    val maxTemp: Double,
    val minTemp: Double,
    val rainProb: Int,
    val condition: String
)
