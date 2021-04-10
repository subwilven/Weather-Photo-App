package com.example.photoweatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherModel (
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val timezone: Long,
    val name: String,
): Parcelable

@Parcelize
data class Main (
    val temp: Double,

    @SerializedName("feels_like")
    val feelsLike: Double,

    @SerializedName("temp_min")
    val tempMin: Double,

    @SerializedName("temp_max")
    val tempMax: Double,

    val pressure: Long,
    val humidity: Long
): Parcelable

@Parcelize
data class Weather (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
): Parcelable

@Parcelize
data class Wind (
    val speed: Double,
    val deg: Long,
    val gust: Double
): Parcelable