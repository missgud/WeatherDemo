package com.example.weatherdemo.logic.model

data class PlaceResponse(val status: String, val geocodes: List<Place>)

data class Place(val formatted_address: String, val province: String, val adcode: String)


