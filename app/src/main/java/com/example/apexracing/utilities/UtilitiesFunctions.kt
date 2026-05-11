package com.example.apexracing.utilities

import com.google.firebase.Timestamp


class UtilitiesFunctions {

    fun getTeamColor(teamName: String): String {
        var color: String
        when (teamName){

            "Mercedes" , "mercedes" ->  color = "#75F1D3"
            "McLaren" , "mclaren" ->  color = "#ef8733"
            "Ferrari" , "ferrari" ->  color = "#D52E37"
            "Red Bull" , "red_bull" ->  color = "#4570C0"
            "Alpine F1 Team" , "alpine" ->  color = "#479FE2"
            "RB F1 Team" , "rb" ->  color = "#7091f8"
            "Williams" , "williams" ->  color = "#3267D4"
            "Audi" , "audi" ->  color = "#EB4526"
            "Haas F1 Team" , "haas" ->  color = "#DFE1E2"
            "Cadillac F1 Team" , "cadillac" ->  color = "#AAAADD"
            "Aston Martin" , "aston_martin" ->  color = "#4B9774"

            "Alfa Romeo" -> color = "#B12039"
            "AlphaTauri" -> color = "#20394C"
            "Racing Point" -> color = "#F363B9"
            "Renault" -> color = "#000000"
            "Toro Rosso" -> color = "#00144A"
            "Force India" -> color = "#F27836"
            "Sauber" -> color = "#01C00E"
            "BMW Sauber" -> color = "#1D1B1E"
            "Manor Marussia" -> color = "#006DC1"
            "Marussia" -> color = "#006DC1"

            "Lotus F1" -> color = "#86995B"
            "Lotus" -> color = "#86995B"
            "Caterham" -> color = "#005030"
            "HRT" -> color = "#A6904F"

            else -> color = "#FFFFFF"



        }
        return color
    }

    fun getTimeSince(date: Timestamp): String {

        val now = System.currentTimeMillis()
        val time = date.toDate().time

        val diff = now - time

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days < 7 -> "${days}d ago"
            weeks < 4 -> "${weeks}w ago"
            months < 12 -> "${months}mo ago"
            else -> "${years}y ago"
        }
    }


        fun getNumFollowing(rank: Int): String {
            var following: String
            when (rank) {
                1 -> following = "st"
                2 -> following = "nd"
                3 -> following = "rd"
                else -> following = "th"
            }
            return following
        }


}