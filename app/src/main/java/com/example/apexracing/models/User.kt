package com.example.apexracing.models

data class User(
    val id: String = "",
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val fantasyBudget: Float,
    val fantasyDriver: List<Driver> = listOf(),
    val fantasyConstructor: List<Constructor> = listOf(),
    val fantasyPoints: Int,
    val favoriteDriver: Driver?,
    val favoriteTeam: Constructor?,
    val imgRef: String
){

fun getGridNum(): Int {
    return fantasyDriver.size + fantasyConstructor.size
}

    fun getBudgetPercentage(): Float{
        return  100 - fantasyBudget
    }
}