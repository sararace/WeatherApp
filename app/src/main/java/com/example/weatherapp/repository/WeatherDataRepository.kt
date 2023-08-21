package com.example.weatherapp.repository

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.network.WeatherForecastApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface WeatherDataRepository {
    val forecast: StateFlow<Forecast?>

    suspend fun fetchWeatherAtLocation(lat: Double, lon: Double)
    suspend fun fetchWeatherInCity(city: String)
}

class WeatherDataRepositoryImpl(private val service: WeatherForecastApi) : WeatherDataRepository {

    override val forecast = MutableStateFlow<Forecast?>(null)

    override suspend fun fetchWeatherAtLocation(lat: Double, lon: Double) {
        val response = service.getWeatherAtLocation(lat, lon)
        if (response.isSuccessful) {
            forecast.emit(response.body())
        }
    }

    override suspend fun fetchWeatherInCity(city: String) {
        val response = service.getWeatherInCity(city)
        if (response.isSuccessful) {
            forecast.emit(response.body())
        }
    }
}