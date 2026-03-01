package com.example.apexracing.models

data class User(
    val username: String,
    val name: String,
    val fantasyBudget: Int,
    val fantasyDriver1: Driver,
    val fantasyDriver2: Driver,
    val fantasyTeam1: Constructor,
    val fantasyTeam2: Constructor,
    val favoriteDriver: Driver,
    val favoriteTeam: Constructor
)