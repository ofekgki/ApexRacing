package com.example.apexracing.utilities

class UtilitiesFunctions {

    fun getTeamColor(teamName: String): String {
        var color: String
        when (teamName){

            "Mercedes" ->  color = "#75F1D3"
            "McLaren" ->  color = "#ef8733"
            "Ferrari" ->  color = "#D52E37"
            "Red Bull" ->  color = "#4570C0"
            "Alpine F1 Team" ->  color = "#479FE2"
            "RB F1 Team" ->  color = "#7091f8"
            "Williams" ->  color = "#3267D4"
            "Audi" ->  color = "#EB4526"
            "Haas F1 Team" ->  color = "#DFE1E2"
            "Cadillac F1 Team" ->  color = "#AAAADD"
            "Aston Martin" ->  color = "#4B9774"

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
}