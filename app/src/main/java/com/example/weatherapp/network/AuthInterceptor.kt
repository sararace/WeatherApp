package com.example.weatherapp.network

import com.example.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url.newBuilder()
            .addQueryParameter("appid", BuildConfig.APP_ID)
            .addQueryParameter("units", "imperial")
            .build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}