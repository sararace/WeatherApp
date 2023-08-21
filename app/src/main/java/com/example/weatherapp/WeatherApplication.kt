package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.modules.appModule
import com.example.weatherapp.modules.networkModule
import com.example.weatherapp.modules.viewModelModule
import org.koin.core.context.GlobalContext.startKoin

class WeatherApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule, networkModule, viewModelModule)
        }
    }
}