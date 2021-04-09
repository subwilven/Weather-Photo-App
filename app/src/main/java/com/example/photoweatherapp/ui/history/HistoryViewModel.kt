package com.example.photoweatherapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoweatherapp.data.Repository
import com.example.photoweatherapp.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(val repository: Repository) :ViewModel() {

    var weatherData: WeatherModel? = null

    init {
        fetchData()
    }

    fun text(){}
    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                weatherData = repository.fetchWeatherData()
            }
        }
    }
}