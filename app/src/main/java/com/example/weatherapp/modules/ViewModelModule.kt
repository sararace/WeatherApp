package com.example.weatherapp.modules

import com.example.weatherapp.MainViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { MainViewModel(get()) }
}