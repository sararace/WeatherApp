package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.model.Coordinates
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.view.CurrentWeather
import com.example.weatherapp.view.SearchBar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

// future improvement: Show a loading animation initially instead of zeroes
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationPermissionGranted = false

    // future improvement: Move these two variables to the view model class
    private var lastKnownLocation: Coordinates? = null
    private val defaultLocation: Coordinates = Coordinates(48.8566, 2.3522)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null &&
            sharedPref.contains(KEY_LATITUDE) &&
            sharedPref.contains(KEY_LONGITUDE)
        ) {
            // future improvement: Make Coordinates class parcelable so we can store it as one
            // value instead of two
            lastKnownLocation = Coordinates(
                sharedPref.getFloat(KEY_LATITUDE, 0f).toDouble(),
                sharedPref.getFloat(KEY_LONGITUDE, 0f).toDouble()
            )
            fetchDataAtLastKnownLocation()
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            getLocationPermission()
            getDeviceLocation()
        }

        lifecycleScope.launch {
            viewModel.currentWeatherCoordinates.collect { coordinates ->
                lastKnownLocation = coordinates
            }
        }

        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        SearchBar(viewModel::fetchWeatherInCity)

                        val currentWeatherUIState by viewModel.currentWeatherUIState.collectAsState()
                        CurrentWeather(currentWeatherUIState)
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // future improvement: Move this logic to the view model and pass in a singleton
        // SharedPreferences object in the constructor of the viewModel using koin
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        lastKnownLocation?.let {
            with(sharedPref.edit()) {
                putFloat(KEY_LATITUDE, it.lat.toFloat())
                putFloat(KEY_LONGITUDE, it.lon.toFloat())
                commit() // apply() causes a race condition while force closing the app
            }
        }
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        if (locationPermissionGranted) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                lastKnownLocation = location?.let { Coordinates(it.latitude, it.longitude) }
                fetchDataAtLastKnownLocation()
            }
        }
    }

    private fun fetchDataAtLastKnownLocation() {
        if (lastKnownLocation != null) {
            viewModel.fetchWeatherAtLocation(lastKnownLocation)
        } else {
            viewModel.fetchWeatherAtLocation(defaultLocation)
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            locationPermissionGranted = when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> true
                else -> false
            }
            getDeviceLocation()
            updateCurrentWeather()
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun updateCurrentWeather() {
        if (locationPermissionGranted) {
            viewModel.fetchWeatherAtLocation(lastKnownLocation)
        } else {
            lastKnownLocation = null
            getLocationPermission()
        }
    }

    companion object {
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
    }
}