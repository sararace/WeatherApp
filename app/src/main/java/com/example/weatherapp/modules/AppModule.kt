package com.example.weatherapp.modules

import com.example.weatherapp.repository.WeatherDataRepository
import com.example.weatherapp.repository.WeatherDataRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<WeatherDataRepository> { WeatherDataRepositoryImpl(get()) }
}