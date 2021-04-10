package com.example.photoweatherapp.ui.history

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoweatherapp.BuildConfig
import com.example.photoweatherapp.data.Repository
import com.example.photoweatherapp.model.WeatherModel
import com.example.photoweatherapp.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class HistoryViewModel(val repository: Repository) : ViewModel() {

    val imagesList = MutableLiveData<MutableList<File>>()
    val onImageAdded = SingleLiveEvent<Int>()
    val onImageClicked = SingleLiveEvent<File>()
    val navigateToFullScreen = SingleLiveEvent<File>()
    val showToast = SingleLiveEvent<String>()

    var weatherData: WeatherModel? = null

    init {
        getSavedImagesFile()
        fetchWeatherData(getDefaultLocation())
    }

    private fun getDefaultLocation():Location{
        showToast.value = "Location still not fetched yet we have used default location"
       return Location(BuildConfig.APPLICATION_ID).apply {
           latitude =  30.025586
           longitude = 31.475227
       }
    }

    fun fetchWeatherData(location: Location) {
        if (weatherData != null) return
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

    fun navigateToFullScreen(file: File) {
        navigateToFullScreen.value = file
    }

}