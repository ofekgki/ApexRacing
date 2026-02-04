package com.example.apexracing.models

data class Race(
    val season: Int,
    val round: Int,
    val raceName: String,
    val circuit: Circuit,
    val date: String,        // "YYYY-MM-DD"
    val time: String?        // "HH:mm:ssZ" sometimes null
)