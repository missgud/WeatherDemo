package com.example.weatherdemo.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weatherdemo.logic.Repository

class WeatherViewModel : ViewModel() {

    private val adCodeLiveData = MutableLiveData<String>()

    var adCode = "110000"

    val weatherLiveData = Transformations.switchMap(adCodeLiveData) { adCode ->
        Repository.refreshWeather(adCode)
    }

    fun refreshWeather(adCode: String) {
        adCodeLiveData.value = adCode
    }

}