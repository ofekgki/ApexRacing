package com.example.apexracing.api

import com.example.apexracing.models.FlatDriverStanding
import com.example.apexracing.utilities.UtilitiesFunctions
import com.google.gson.*
import java.lang.reflect.Type

class FlatDriverDeserializer : JsonDeserializer<FlatDriverStanding> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FlatDriverStanding {
        val jsonObject = json.asJsonObject

        val position = jsonObject.get("position").asString
        val points = jsonObject.get("points").asString
        val driverObj = jsonObject.getAsJsonObject("Driver")
        val fullName = "${driverObj.get("givenName").asString} ${driverObj.get("familyName").asString}"
        val teamName = jsonObject.getAsJsonArray("Constructors").get(0).asJsonObject.get("name").asString
        val color = UtilitiesFunctions().getTeamColor(teamName)
        return FlatDriverStanding(points.toInt(), position.toInt(), fullName,
            teamName,color)
    }
}