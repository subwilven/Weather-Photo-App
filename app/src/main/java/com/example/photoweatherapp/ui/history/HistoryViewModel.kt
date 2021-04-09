package com.example.photoweatherapp.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoweatherapp.data.Repository
import com.example.photoweatherapp.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HistoryViewModel(val repository: Repository) :ViewModel() {

    val imagesList = MutableLiveData<MutableList<File>>(mutableListOf())
    val onImageAdded = MutableLiveData<Int>()
    var weatherData: WeatherModel? = null

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                weatherData = repository.fetchWeatherData()
            }
        }
    }

    fun addImageItem(file: File) {
        imagesList.value?.add(file)
        onImageAdded.value = imagesList.value?.count()
    }

    fun getWeatherDataInfo():WeatherModel?{
        return weatherData
    }

}