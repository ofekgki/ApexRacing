package com.example.apexracing.api.DTO

import com.google.gson.annotations.SerializedName

data class MrDataDto<T>(
    @SerializedName("total") val total: String?,
    @SerializedName("limit") val limit: String?,
    @SerializedName("offset") val offset: String?,
    @SerializedName("DriverTable") val driverTable: DriverTableDto? = null,
    @SerializedName("ConstructorTable") val constructorTable: ConstructorTableDto? = null,
    @SerializedName("RaceTable") val raceTable: RaceTableDto? = null,
    @SerializedName("StandingsTable") val standingsTable: StandingsTableDto? = null
)

data class ErgastResponseDto(
    @SerializedName("MRData") val mrData: MrDataDto<Any>
)
