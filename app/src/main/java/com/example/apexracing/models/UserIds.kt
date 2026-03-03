package com.example.apexracing.models

data class UserIds(
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val fantasyBudget: Int = 100,
    val fantasyDriver1: String = "",
    val fantasyDriver2: String = "",
    val fantasyTeam1: String = "",
    val fantasyTeam2: String = "",
    val fantasyPoints: Int = 0,
    val favoriteDriver: String = "",
    val favoriteTeam: String = ""
)