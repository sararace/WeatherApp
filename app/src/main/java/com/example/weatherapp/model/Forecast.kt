package com.example.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    val coord: Coordinates,
    val weather: List<Weather>,
    val main: Main,
    val timezone: Int,
    val id: String,
    val name: String
)

data class Coordinates(
    val lat: Double,
    val lon: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double
)