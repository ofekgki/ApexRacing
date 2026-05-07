package com.example.apexracing.api
import com.example.apexracing.models.FlatDriverStanding
import com.example.apexracing.utilities.UtilitiesFunctions
import com.google.gson.*
import java.lang.reflect.Type

class StandingDriverDeserializer: JsonDeserializer<List<FlatDriverStanding>> {


    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<FlatDriverStanding> {
        return parse(json)
    }

    fun parse(json: JsonElement): List<FlatDriverStanding> {

        val parsedList = mutableListOf<FlatDriverStanding>()

        try {

            val standingsLists = json.asJsonObject
                .getAsJsonObject("MRData")
                .getAsJsonObject("StandingsTable")
                .getAsJsonArray("StandingsLists")

            val driverStandingsArray = standingsLists[0]
                .asJsonObject
                .getAsJsonArray("DriverStandings")

            for (element in driverStandingsArray) {

                val item = element.asJsonObject

                val position = item.get("position").asString.toInt()
                val points = item.get("points").asString.toDouble().toInt()

                val driverObj = item.getAsJsonObject("Driver")

                val fullName =
                    "${driverObj.get("givenName").asString} " +
                            "${driverObj.get("familyName").asString}"

                val teamName = item.getAsJsonArray("Constructors").get(0)
                    .asJsonObject.get("name").asString

                val color = UtilitiesFunctions().getTeamColor(teamName)

                parsedList.add(
                    FlatDriverStanding(
                        points = points,
                        position = position,
                        fullName = fullName,
                        constructorName = teamName,
                        color = color
                    )
                )
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return parsedList
    }
    }
