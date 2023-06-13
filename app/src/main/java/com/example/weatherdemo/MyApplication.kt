package com.example.weatherdemo

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class
MyApplication : Application() {

    companion object {

        const val KEY = "bc588424e3d9eb909c4183beae153d07"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}