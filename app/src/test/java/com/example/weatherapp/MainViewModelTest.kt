package com.example.weatherapp

import com.example.weatherapp.model.Coordinates
import com.example.weatherapp.repository.FakeWeatherDataRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val weatherDataRepository = FakeWeatherDataRepository()
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        mainViewModel = MainViewModel(weatherDataRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Future improvement: use awaitItem() from turbine library instead of advanceTimeBy()
    @Test
    fun testFetchWeatherAtLocation() = runTest {
        launch(Dispatchers.Main) {
            mainViewModel.fetchWeatherAtLocation(Coordinates(1.0, 1.0))
            advanceTimeBy(100)
            assertThat(weatherDataRepository.wasWeatherFetched).isTrue()
        }
    }

    /* Future improvement: add more unit tests. This one is not particularly useful as it's
     * currently written, but I just wrote it as an example of how to use the repository
     * architecture to mock the data in the tests.
     */
}