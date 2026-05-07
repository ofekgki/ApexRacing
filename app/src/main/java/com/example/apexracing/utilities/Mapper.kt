package com.example.apexracing.utilities

import com.example.apexracing.models.User
import com.example.apexracing.models.UserIds

object Mapper {

    fun userMapper(dto: UserIds): User {
        val driversById = DBData.drivers.associateBy { it.id }
        val teamsById = DBData.teams.associateBy { it.id }

        val fantasyDrivers = dto.fantasyDriverIds
            .asSequence()
            .filter { it.isNotBlank() }
            .distinct()
            .mapNotNull { id -> driversById[id] }
            .toList()
        val fantasyTeams = dto.fantasyConstructorIds
            .asSequence()
            .filter { it.isNotBlank() }
            .distinct()
            .mapNotNull { id -> teamsById[id] }
            .toList()

        val user = User(
            id = dto.id,
            username = dto.username,
            firstName = dto.firstName,
            lastName = dto.lastName,
            email = dto.email,
            fantasyBudget = dto.fantasyBudget,
            fantasyDriver = fantasyDrivers,
            fantasyConstructor = fantasyTeams,
            fantasyPoints = dto.fantasyPoints,
            favoriteDriver = DBData.drivers.find { it.id == dto.favoriteDriver },
            favoriteTeam = DBData.teams.find { it.id == dto.favoriteTeam },
            imgRef = dto.imgRef

        )
        return user
    }
}