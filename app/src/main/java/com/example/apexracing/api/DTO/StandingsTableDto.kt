package com.example.apexracing.api.DTO

import com.google.gson.annotations.SerializedName

data class StandingsTableDto(
    @SerializedName("season") val season: String?,
    @SerializedName("round") val round: String?,
    @SerializedName("StandingsLists") val standingsLists: List<StandingsListDto> = emptyList()
)

data class StandingsListDto(
    @SerializedName("DriverStandings") val driverStandings: List<DriverStandingDto> = emptyList(),
    @SerializedName("ConstructorStandings") val constructorStandings: List<ConstructorStandingDto> = emptyList()
)

data class DriverStandingDto(
    val position: String?,
    val points: String?,
    val wins: String?,
    @SerializedName("Driver") val driver: DriverDto?,
    @SerializedName("Constructors") val constructors: List<ConstructorDto> = emptyList()
)

data class ConstructorStandingDto(
    val position: String?,
    val points: String?,
    val wins: String?,
    @SerializedName("Constructor") val constructor: ConstructorDto?
)