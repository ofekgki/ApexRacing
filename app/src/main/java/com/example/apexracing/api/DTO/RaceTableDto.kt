package com.example.apexracing.api.DTO


import com.google.gson.annotations.SerializedName

data class RaceTableDto(
    @SerializedName("season") val season: String?,
    @SerializedName("Races") val races: List<RaceDto> = emptyList()
)

data class RaceDto(
    val season: String?,
    val round: String?,
    val raceName: String?,
    val Circuit: CircuitDto?,
    val date: String?,
    val time: String?,
    @SerializedName("Results") val results: List<RaceResultDto>? = null
)

data class CircuitDto(
    val circuitId: String?,
    val circuitName: String?,
)