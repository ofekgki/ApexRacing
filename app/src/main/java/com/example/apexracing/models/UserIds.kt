package com.example.apexracing.models

data class UserIds(
    val id: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val fantasyBudget: Float = 100F,
    val fantasyDriverIds: List<String> = listOf(),
    val fantasyConstructorIds: List<String> = listOf(),
    val fantasyPoints: Int = 0,
    val favoriteDriver: String = "",
    val favoriteTeam: String = "",
    val imgRef: String = ""
)