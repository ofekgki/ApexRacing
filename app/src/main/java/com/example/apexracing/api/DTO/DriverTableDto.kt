package com.example.apexracing.api.DTO


import com.google.gson.annotations.SerializedName

data class DriverTableDto(
    @SerializedName("season") val season: String?,
    @SerializedName("Drivers") val drivers: List<DriverDto> = emptyList()
)

data class DriverDto(
    val permanentNumber: String?,
    val givenName: String?,
    val familyName: String?,
    val nationality: String?
)