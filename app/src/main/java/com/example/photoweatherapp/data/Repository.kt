package com.example.photoweatherapp.data

import com.example.photoweatherapp.utils.API_KEY

class Repository(private val weatherApis: WeatherApis) {

    suspend fun fetchWeatherData() = weatherApis.fetchWeather("35","139", API_KEY)

}