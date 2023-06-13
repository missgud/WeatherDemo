package com.example.weatherdemo.logic.network

import com.example.weatherdemo.MyApplication
import com.example.weatherdemo.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("v3/geocode/geo?output=JSON&key=${MyApplication.KEY}")
    fun searchPlaces(@Query("address") address: String): Call<PlaceResponse>

}