package com.example.weatherapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun CurrentWeather(uiState: MainViewModel.CurrentWeatherUIState) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = uiState.cityName,
            fontSize = 28.sp
        )
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Use coil to load the image. Normally I prefer Glide to load images since it has
            // a built-in cache, but their compose support is still in alpha.
            AsyncImage(
                modifier = Modifier.size(100.dp),
                model = uiState.iconUrl,
                contentDescription = uiState.weatherName
            )
            Text(
                text = stringResource(id = R.string.degrees_fahrenheit, uiState.temperature),
                fontSize = 64.sp
            )
        }
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = uiState.weatherName,
            fontSize = 20.sp
        )
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.max_degrees_fahrenheit, uiState.tempMax),
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = stringResource(id = R.string.min_degrees_fahrenheit, uiState.tempMin),
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentWeatherPreview() {
    WeatherAppTheme {
        CurrentWeather(
            MainViewModel.CurrentWeatherUIState(
                90,
                "Helsinki",
                "https://openweathermap.org/img/wn/10d@2x.png",
                "Clear",
                95,
                70
            )
        )
    }
}