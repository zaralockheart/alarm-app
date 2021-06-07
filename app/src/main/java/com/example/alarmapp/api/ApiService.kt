package com.example.alarmapp.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit client
 *
 * reference:
 * https://square.github.io/retrofit/
 */
interface ApiService {
    @GET("data/2.5/forecast")
    suspend fun getUsers(@Query("q") location: String, @Query("appid") appid: String): WeatherResponse
}

object RetrofitBuilder {
    private const val BASE_URL = "https://api.openweathermap.org/"

    private fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}