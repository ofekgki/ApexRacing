package com.example.apexracing.api

import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("ergast/f1/{year}/driverStandings.json")
    suspend fun getDriverStandings(
        @Path("year") year: String
    ): JsonElement

    @GET("ergast/f1/{year}/constructorStandings.json")
    suspend fun getConstructorStandings(
        @Path("year") year: String
    ): JsonElement
}