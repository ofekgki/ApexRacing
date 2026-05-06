package com.example.apexracing.api

import com.example.apexracing.models.FlatConstructorStanding
import com.example.apexracing.utilities.UtilitiesFunctions
import com.google.gson.*
import java.lang.reflect.Type

class FlatConstructorDeserializer : JsonDeserializer<FlatConstructorStanding> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): FlatConstructorStanding {
        val jsonObject = json.asJsonObject

        val position = jsonObject.get("position").asString
        val points = jsonObject.get("points").asString
        val constructorObj = jsonObject.getAsJsonObject("Constructors")
        val name = "${constructorObj.get("name").asString}"
        val color = UtilitiesFunctions().getTeamColor(name)

        return FlatConstructorStanding(points.toInt(), position.toInt(), name, color)
    }
}