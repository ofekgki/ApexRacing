package com.example.apexracing.api.DTO

import com.google.gson.annotations.SerializedName

data class ConstructorTableDto(
    @SerializedName("season") val season: String?,
    @SerializedName("Constructors") val constructors: List<ConstructorDto> = emptyList()
)

data class ConstructorDto(
    val constructorId: String?,
    val name: String?,
    val nationality: String?
)