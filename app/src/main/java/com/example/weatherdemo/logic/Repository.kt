package com.example.weatherdemo.logic

import androidx.lifecycle.liveData
import com.example.weatherdemo.logic.model.Weather
import com.example.weatherdemo.logic.network.WeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = WeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "1") {
            val places = placeResponse.geocodes
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(adCode: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                WeatherNetwork.getRealtimeWeather(adCode)
            }
            val deferredDaily = async {
                WeatherNetwork.getDailyWeather(adCode)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "1" && dailyResponse.status == "1") {
                val weather = Weather(realtimeResponse, dailyResponse)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}