package com.example.weatherdemo.logic.network

import com.example.weatherdemo.MyApplication
import com.example.weatherdemo.logic.model.DailyResponse
import com.example.weatherdemo.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("v3/weather/weatherInfo?extensions=base&key=${MyApplication.KEY}")
    fun getRealtimeWeather(@Query("city") city: String): Call<RealtimeResponse>

    @GET("v3/weather/weatherInfo?extensions=all&key=${MyApplication.KEY}")
    fun getDailyWeather(@Query("city") city: String): Call<DailyResponse>

}