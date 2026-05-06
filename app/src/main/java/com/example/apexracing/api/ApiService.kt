package com.example.apexracing.api

import com.example.apexracing.models.FlatConstructorStanding
import com.example.apexracing.models.FlatDriverStanding
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("ergast/f1/{year}/driverstandings/")
    suspend fun getDriverStandings(
        @Path("year") year: String
    ): List<FlatDriverStanding>

    @GET("ergast/f1/{year}/constructorstandings/")
    suspend fun getConstructorStandings(
        @Path("year") year: String
    ): List<FlatConstructorStanding>
}