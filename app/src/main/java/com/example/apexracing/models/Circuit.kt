package com.example.apexracing.models

import com.google.firebase.firestore.GeoPoint
import java.util.Date


data class Circuit(
    var id: String,
    var circuitName: String,
    var displayName: String,
    var city: String,
    var country: String,
    var layoutRef: String? = null,
    var location: GeoPoint?,
    var startTime: Date,
    var round: Int

)
{
    constructor() : this(
        id = "",
        circuitName = "",
        displayName = "",
        city = "",
        country = "",
        layoutRef = "",
        location = GeoPoint(0.0,0.0),
        startTime = Date(),
        round = 0
    )

    fun getMonth(): String {
        return java.text
            .SimpleDateFormat("MMM", java.util.Locale.US)
            .format(startTime)
    }


}
