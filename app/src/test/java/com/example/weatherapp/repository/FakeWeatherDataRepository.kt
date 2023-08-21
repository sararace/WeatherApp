package com.example.weatherapp.repository

import com.example.weatherapp.model.Forecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class FakeWeatherDataRepository : WeatherDataRepository {
    override val forecast: StateFlow<Forecast?> = MutableStateFlow(null)
    var wasWeatherFetched = false

    override suspend fun fetchWeatherAtLocation(lat: Double, lon: Double) {
        wasWeatherFetched = true
    }

    override suspend fun fetchWeatherInCity(city: String) {
        wasWeatherFetched = true
    }
}