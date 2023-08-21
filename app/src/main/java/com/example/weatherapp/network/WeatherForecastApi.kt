package com.example.weatherapp.network

import com.example.weatherapp.model.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherForecastApi {
    @GET("weather")
    suspend fun getWeatherAtLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<Forecast>

    @GET("weather")
    suspend fun getWeatherInCity(
        @Query("q") city: String
    ): Response<Forecast>
}