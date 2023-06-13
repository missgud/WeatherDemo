package com.example.weatherdemo.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.weatherdemo.R
import com.example.weatherdemo.logic.model.Weather
import com.example.weatherdemo.logic.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.now.*


class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)
        if (viewModel.adCode.isEmpty()) {
            viewModel.adCode = intent.getStringExtra("ad_code") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    drawerView.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.adCode)
        swipeRefresh.isRefreshing = true
    }

    @SuppressLint("SetTextI18n")
    private fun showWeatherInfo(weather: Weather) {
        val realtimeInfo = weather.realtime.lives[0]
        placeName.text = realtimeInfo.province
        // 填充now.xml布局中数据
        val currentTempText = "${realtimeInfo.temperature} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtimeInfo.weather).info
        cityName.text = realtimeInfo.city
        val info =
            "风向: ${realtimeInfo.winddirection} | 风力级别: ${realtimeInfo.windpower} | 空气湿度 : ${realtimeInfo.humidity}"
        otherInfo.text = info
        nowDate.text = realtimeInfo.reporttime
        nowLayout.setBackgroundResource(getSky(realtimeInfo.weather).bg)
        // 填充forecast.xml布局中的数据
        val daily = weather.daily
        forecastLayout.removeAllViews()
        val days = daily.forecasts[0].casts.size
        for (i in 0 until days) {
            val skycon = daily.forecasts[0].casts[i]
            val view =
                LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            dateInfo.text = skycon.date
            val weekInfo = view.findViewById(R.id.week) as TextView
            weekInfo.text = "星期${getWeek(skycon.week)}"
            // 白天信息
            val dayWeather = view.findViewById(R.id.dayWeather) as TextView
            dayWeather.text = skycon.dayweather
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val sky = getSky(skycon.dayweather)
            skyIcon.setImageResource(sky.icon)
            val dayTemp = view.findViewById(R.id.dayTemp) as TextView
            dayTemp.text = "${skycon.daytemp} ℃"
            val dayWind = view.findViewById(R.id.dayWind) as TextView
            dayWind.text = "风向: ${skycon.daywind}"
            val dayWindPower = view.findViewById(R.id.dayWindPower) as TextView
            dayWindPower.text = "风力: ${skycon.daypower}"
            // 晚上信息
            val nightWeather = view.findViewById(R.id.nightWeather) as TextView
            nightWeather.text = skycon.nightweather
            val nightIcon = view.findViewById(R.id.nightIcon) as ImageView
            val nightSky = getSky(skycon.nightweather)
            nightIcon.setImageResource(nightSky.icon)
            val nightTemp = view.findViewById(R.id.nightTemp) as TextView
            nightTemp.text = "${skycon.nighttemp} ℃"
            val nightWind = view.findViewById(R.id.nightWind) as TextView
            nightWind.text = "风向: ${skycon.nightwind}"
            val nightWindPower = view.findViewById(R.id.nightWindPower) as TextView
            nightWindPower.text = "风力: ${skycon.nightpower}"

            forecastLayout.addView(view)
        }

    }

    private fun getWeek(week: String) =
        when (week) {
            "1" -> "一"
            "2" -> "二"
            "3" -> "三"
            "4" -> "四"
            "5" -> "五"
            "6" -> "六"
            "7" -> "日"
            else -> ""

        }
}
