package com.example.apexracing.models

import android.location.Location
import java.util.Date


data class Circuit(
    val id: String,
    val circuitName: String,
    val displayName: String,
    val city: String,
    val country: String,
    val layoutRef: String,
    val location: Location,
    val startTime: Date

)