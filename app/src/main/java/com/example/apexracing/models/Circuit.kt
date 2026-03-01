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
    val startTime: Date,
    val round: Int

)
{
    constructor() : this(
        id = "",
        circuitName = "",
        displayName = "",
        city = "",
        country = "",
        layoutRef = "",
        location = Location(""),
        startTime = Date(),
        round = 0
    )

    class Builder(
        var id: String = "",
        var circuitName: String = "",
        var displayName: String = "",
        var city: String = "",
        var country: String = "",
        var layoutRef: String = "",
        var location: Location = Location(""),
        var startTime: Date = Date(),
        var round: Int = 0


    ) {

        fun id(id: String) = apply { this.id = id }
        fun circuitName(circuitName: String) = apply { this.circuitName = circuitName }
        fun displayName(displayName: String) = apply { this.displayName = displayName }
        fun city(city: String) = apply { this.city = city }
        fun country(country: String) = apply { this.country = country }
        fun layoutRef(layoutRef: String) = apply { this.layoutRef = layoutRef }
        fun location(location: Location) = apply { this.location = location }
        fun startTime(startTime: Date) = apply { this.startTime = startTime }
        fun round(round: Int) = apply { this.round = round }
        fun build() = Circuit(
            id,
            circuitName,
            displayName,
            city,
            country,
            layoutRef,
            location,
            startTime,
            round
        )


    }

    fun getMonth(): String{
        val month = startTime.month

        return when(month){
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> "Unknown"
        }

    }
}
