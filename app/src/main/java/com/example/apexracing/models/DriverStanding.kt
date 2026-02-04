package com.example.apexracing.models

data class DriverStanding(
    val position: Int,
    val points: Double,
    val wins: Int,
    val driver: Driver,
    val constructors: List<Constructor>
)