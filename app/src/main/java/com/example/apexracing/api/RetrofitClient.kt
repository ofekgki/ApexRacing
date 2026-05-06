package com.example.apexracing.api

import com.example.apexracing.models.FlatConstructorStanding
import com.example.apexracing.models.FlatDriverStanding
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitClient {

    private const val BASE_URL = "https://api.jolpi.ca/"

    //by lazy -> מופע הסינגלטון נוצר בפעם הראשונה שנקרא לו
    private val customDriverGson by lazy { // Custom Gson instance with our custom deserializer
        val listType = object : TypeToken<List<FlatDriverStanding>>() {}.type
        GsonBuilder()
            .registerTypeAdapter(listType, StandingDriverDeserializer())
            .create()
    }
    private val customConstructorGson by lazy { // Custom Gson instance with our custom deserializer
        val listType = object : TypeToken<List<FlatConstructorStanding>>() {}.type
        GsonBuilder()
            .registerTypeAdapter(listType, StandingConstructorDeserializer())
            .create()
    }

    private val okHttpClient by lazy { // OkHttpClient with logging interceptor for debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val constructorsApiService: ApiService by lazy { // Retrofit instance with custom Gson and OkHttpClient
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customConstructorGson))
            .build()
            .create(ApiService::class.java)
    }

    val driversApiService: ApiService by lazy { // Retrofit instance with custom Gson and OkHttpClient
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customDriverGson))
            .build()
            .create(ApiService::class.java)
    }
}