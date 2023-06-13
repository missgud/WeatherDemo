package com.example.weatherdemo.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.weatherdemo.logic.Repository
import com.example.weatherdemo.logic.model.Place

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = mutableListOf<Place>(
        Place("北京市", "北京市", "110000"),
        Place("上海市", "上海市", "310000"),
        Place("广东省", "广东省广州市", "440100"),
        Place("广东省", "广东省深圳市", "440300"),
        Place("江苏省", "江苏省苏州市", "320500"),
        Place("辽宁省", "辽宁省沈阳市", "210100")
    )

    val placeLiveData = Transformations.switchMap(searchLiveData) { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

}