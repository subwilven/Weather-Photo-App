package com.example.photoweatherapp.ui.history

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoweatherapp.data.Repository
import com.example.photoweatherapp.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HistoryViewModel(val repository: Repository) : ViewModel() {

    val imagesList = MutableLiveData<MutableList<File>>()
    val onImageAdded = MutableLiveData<Int>()
    val onImageClicked = MutableLiveData<File>()
    var weatherData: WeatherModel? = null

    init {
        getSavedImagesFile()
    }

    fun fetchWeatherData(location: Location) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                weatherData = repository.fetchWeatherData(location)
            }
        }
    }

    private fun getSavedImagesFile() {
        imagesList.value = repository.getSavedImagesList()
    }

    fun addImageItem(file: File) {
        imagesList.value?.add(file)
        onImageAdded.value = imagesList.value?.count()
    }

    fun getWeatherDataInfo(): WeatherModel? {
        return weatherData
    }

    fun shareImage(file: File) {
        onImageClicked.value = file
    }

}