package com.example.apexracing.models

import com.google.firebase.firestore.GeoPoint
import java.util.Date


data class Circuit(
    var id: String,
    var circuitName: String?,
    var displayName: String?,
    var city: String,
    var country: String,
    var layoutRef: String? = null,
    var location: GeoPoint?,
    var startTime: Date,
    var round: Int,
    var flagRef: String,
    var skylineRef: String

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
        round = 0,
        flagRef = "",
        skylineRef = ""
    )

    fun getMonth(): String {
        return java.text
            .SimpleDateFormat("MMM", java.util.Locale.US)
            .format(startTime)
    }

    class Builder(
        var id: String = "",
        var circuitName: String? = "",
        var displayName: String? = "",
        var city: String = "",
        var country: String = "",
        var layoutRef: String? = "",
        var location: GeoPoint? = null,
        var startTime: Date = Date(),
        var round: Int = 0,
        var flagRef: String = "",
        var skylineRef: String = ""


    ) {

        fun id(id: String) = apply { this.id = id }
        fun circuitName(circuitName: String) = apply { this.circuitName = circuitName }
        fun displayName(displayName: String) = apply { this.displayName = displayName }
        fun city(city: String) = apply { this.city = city }
        fun country(country: String) = apply { this.country = country }
        fun layoutRef(layoutRef: String) = apply { this.layoutRef = layoutRef }
        fun location(location: GeoPoint) = apply { this.location = location }
        fun startTime(startTime: Date) = apply { this.startTime = startTime }
        fun round(round: Int) = apply { this.round = round }
        fun flagRef(flagRef: String) = apply { this.flagRef = flagRef }
        fun skylineRef(skylineRef: String) = apply { this.skylineRef = skylineRef }
        fun build() = Circuit(
            id,
            circuitName,
            displayName,
            city,
            country,
            layoutRef,
            location,
            startTime,
            round,
            flagRef,
            skylineRef
        )


    }


}
