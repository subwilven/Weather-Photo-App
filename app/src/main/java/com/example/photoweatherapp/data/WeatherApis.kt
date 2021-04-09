package com.example.photoweatherapp.data

import com.example.photoweatherapp.model.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApis {

    @GET("weather")
    suspend fun fetchWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
    ): WeatherModel
}