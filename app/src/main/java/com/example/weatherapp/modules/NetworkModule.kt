package com.example.weatherapp.modules

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.network.AuthInterceptor
import com.example.weatherapp.network.WeatherForecastApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { AuthInterceptor() }
    factory { provideHttpLoggingInterceptor() }
    factory { provideOkHttpClient(get(), get()) }
    factory { provideForecastApi(get()) }
    single { provideRetrofit(get()) }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
}

fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor
): OkHttpClient {
    val client = OkHttpClient().newBuilder().addInterceptor(authInterceptor)
    if (DEBUG) {
        client.addInterceptor(httpLoggingInterceptor)
    }
    return client.build()
}

fun provideForecastApi(retrofit: Retrofit): WeatherForecastApi =
    retrofit.create(WeatherForecastApi::class.java)

const val DEBUG = true