package com.example.apexracing.utilities

class UtilitiesFunctions {

    fun getTeamColor(teamName: String): String {
        var color: String = ""
        when (teamName){

            "mercedes" ->  color = "#18FFFF"
            "mcLaren" ->  color = "#F57C00"
            "ferrari" ->  color = "#D50000"
            "red_bull" ->  color = "#F57C00"
            "alpine" ->  color = "#F57C00"
            "racing_bulls" ->  color = "#F57C00"
            "williams" ->  color = "#F57C00"
            "audi" ->  color = "#F57C00"
            "haas" ->  color = "#F57C00"
            "cadillac" ->  color = "#F57C00"
            "aston_martin" ->  color = "#F57C00"



        }
        return color
    }
}