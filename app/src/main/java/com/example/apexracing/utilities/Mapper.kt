package com.example.apexracing.utilities

import com.example.apexracing.models.User
import com.example.apexracing.models.UserIds

object Mapper {

    fun userMapper(dto: UserIds): User {
        val user = User(
            username = dto.username,
            firstName = dto.firstName,
            lastName = dto.lastName,
            email = dto.email,
            fantasyBudget = dto.fantasyBudget,
            fantasyDriver1 = DBData.drivers.find { it.id == dto.fantasyDriver1 },
            fantasyDriver2 = DBData.drivers.find { it.id == dto.fantasyDriver2 },
            fantasyTeam1 = DBData.teams.find { it.id == dto.fantasyTeam1 },
            fantasyTeam2 = DBData.teams.find { it.id == dto.fantasyTeam2 },
            fantasyPoints = dto.fantasyPoints,
            favoriteDriver = DBData.drivers.find { it.id == dto.favoriteDriver },
            favoriteTeam = DBData.teams.find { it.id == dto.favoriteTeam }
        )
        return user
    }
}