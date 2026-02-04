package com.example.apexracing.models

data class RaceResult(
    val position: Int,
    val points: Double,
    val grid: Int?,
    val laps: Int?,
    val status: String?,
    val driver: Driver,
    val constructor: Constructor,
    val time: String?
)