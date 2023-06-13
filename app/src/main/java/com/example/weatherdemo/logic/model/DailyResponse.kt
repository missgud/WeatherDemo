package com.example.weatherdemo.logic.model

data class DailyResponse(val status: String, val forecasts: List<Forecasts>)

data class Forecasts(
    val province: String,
    val city: String,
    val adcode: String,
    val reporttime: String,
    val casts: List<Casts>
)

data class Casts(
    val date: String,
    val week: String,
    val dayweather: String,
    val nightweather: String,
    val daytemp: String,
    val nighttemp: String,
    val daywind: String,
    val nightwind: String,
    val daypower: String,
    val nightpower: String,
    val daytemp_float: String,
    val nighttemp_float: String
)
