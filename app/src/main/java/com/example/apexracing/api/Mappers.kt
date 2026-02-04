package com.example.apexracing.api

import com.example.apexracing.api.DTO.*
import com.example.apexracing.models.*


private fun String?.toIntSafe(default: Int = 0) = this?.toIntOrNull() ?: default
private fun String?.toDoubleSafe(default: Double = 0.0) = this?.toDoubleOrNull() ?: default

fun DriverDto.toDomain(): Driver {
    val given = givenName.orEmpty()
    val family = familyName.orEmpty()
    val full = (given + " " + family).trim()

    return Driver(
        fullName = if (full.isBlank()) "Unknown Driver" else full,
        givenName = given,
        familyName = family,
        permanentNumber = permanentNumber,
        nationality = nationality
    )
}

fun ConstructorDto.toDomain(): Constructor =
    Constructor(
        id = constructorId.orEmpty(),
        name = name.orEmpty(),
        nationality = nationality,
    )


fun CircuitDto.toDomain(): Circuit =
    Circuit(
        id = circuitId.orEmpty(),
        name = circuitName.orEmpty(),
    )

fun RaceDto.toDomain(): Race =
    Race(
        season = season.toIntSafe(),
        round = round.toIntSafe(),
        raceName = raceName.orEmpty(),
        circuit = Circuit?.toDomain() ?: Circuit("", ""),
        date = date.orEmpty(),
        time = time
    )

fun DriverStandingDto.toDomain(): DriverStanding =
    DriverStanding(
        position = position.toIntSafe(),
        points = points.toDoubleSafe(),
        wins = wins.toIntSafe(),
        driver = (driver ?: DriverDto(null,null,null,null)).toDomain(),
        constructors = constructors.map { it.toDomain() }
    )

fun ConstructorStandingDto.toDomain(): ConstructorStanding =
    ConstructorStanding(
        position = position.toIntSafe(),
        points = points.toDoubleSafe(),
        wins = wins.toIntSafe(),
        constructor = (constructor ?: ConstructorDto(null,null,null)).toDomain()
    )

fun RaceResultDto.toDomain(): RaceResult =
    RaceResult(
        position = position.toIntSafe(),
        points = points.toDoubleSafe(),
        grid = grid?.toIntOrNull(),
        laps = laps?.toIntOrNull(),
        status = status,
        driver = (driver ?: DriverDto(null,null,null,null)).toDomain(),
        constructor = (constructor ?: ConstructorDto(null,null,null)).toDomain(),
        time = time?.time
    )
