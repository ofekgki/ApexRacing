package com.example.apexracing.api
import com.example.apexracing.api.DTO.DriverTableDto

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JolpicaApi {

    @GET("f1/{season}/drivers.json")
    suspend fun getDrivers(
        @Path("season") season: String = "current",
        @Query("limit") limit: Int = 1000
    ): DriverTableDto

}