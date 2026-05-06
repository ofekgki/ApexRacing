package com.example.apexracing.api

import com.example.apexracing.models.FlatConstructorStanding
import com.example.apexracing.utilities.UtilitiesFunctions
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class StandingConstructorDeserializer : JsonDeserializer<List<FlatConstructorStanding>> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext):
            List<FlatConstructorStanding> {
        val parsedList = mutableListOf<FlatConstructorStanding>()

        try {

            val constructorStandingsArray = json.asJsonObject
                .getAsJsonObject("MRData")
                .getAsJsonObject("StandingsTable")
                .getAsJsonArray("StandingsLists")
                .get(0).asJsonObject
                .getAsJsonArray("ConstructorStandings")

            for (element in constructorStandingsArray) {
                val item = element.asJsonObject

                val position = item.get("position").asString.toInt()
                val points = item.get("points").asString.toInt()

                val constructorObj = item.getAsJsonObject("Constructor")

                val teamName = constructorObj.get("name").asString

                val color = UtilitiesFunctions().getTeamColor(teamName)

                parsedList.add(FlatConstructorStanding(points, position, teamName, color))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return parsedList
    }
}