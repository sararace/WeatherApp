package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.Coordinates
import com.example.weatherapp.repository.WeatherDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class MainViewModel(private val repository: WeatherDataRepository) : ViewModel() {

    private val weatherData = repository.forecast

    val currentWeatherUIState = weatherData.map {
        CurrentWeatherUIState(
            temperature = it?.main?.temp?.roundToInt() ?: 0,
            cityName = it?.name ?: "",
            iconUrl = getIconUrl(it?.weather?.firstOrNull()?.icon ?: ""),
            weatherName = it?.weather?.firstOrNull()?.main ?: "",
            tempMax = it?.main?.tempMax?.roundToInt() ?: 0,
            tempMin = it?.main?.tempMin?.roundToInt() ?: 0
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, CurrentWeatherUIState())

    val currentWeatherCoordinates = weatherData.map { it?.coord }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Coordinates(0.0, 0.0))

    private fun getIconUrl(icon: String): String {
        return "https://openweathermap.org/img/wn/$icon@2x.png"
    }

    fun fetchWeatherAtLocation(location: Coordinates?) {
        viewModelScope.launch {
            repository.fetchWeatherAtLocation(location?.lat ?: 0.0, location?.lon ?: 0.0)
        }
    }

    fun fetchWeatherInCity(city: String) {
        viewModelScope.launch {
            repository.fetchWeatherInCity(city)
        }
    }

    // Use a UI State in order to reduce the number of parameters needed in the Composables
    data class CurrentWeatherUIState(
        val temperature: Int = 0,
        val cityName: String = "",
        val iconUrl: String = "",
        val weatherName: String = "",
        val tempMax: Int = 0,
        val tempMin: Int = 0
    )
}