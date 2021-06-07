package com.example.alarmapp.ui.weatherforecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alarmapp.api.RetrofitBuilder
import com.example.alarmapp.api.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherForecastViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private val _weathers = MutableLiveData<WeatherResponse>()
    val weathers: LiveData<WeatherResponse> = _weathers

    fun getWeather(location: String) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                try {
                    val response =
                        RetrofitBuilder.apiService.getUsers(
                            location,
                            "f5494a7c084b83729a1ef81f70d182f6"
                        )
                    _weathers.value = response
                } catch (e: HttpException) {
                } catch (e: Throwable) {
                }
            }
        }
    }
}