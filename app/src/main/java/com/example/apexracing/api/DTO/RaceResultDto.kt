package com.example.apexracing.api.DTO


import com.google.gson.annotations.SerializedName

data class RaceResultDto(
    val number: String?,
    val position: String?,
    val points: String?,
    val grid: String?,
    val laps: String?,
    val status: String?,
    @SerializedName("Driver") val driver: DriverDto?,
    @SerializedName("Constructor") val constructor: ConstructorDto?,
    @SerializedName("Time") val time: ResultTimeDto?
)

data class ResultTimeDto(
    val millis: String?,
    val time: String?
)
