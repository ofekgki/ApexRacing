package com.example.apexracing.models

data class Constructor private constructor(
    val id: String = "",
    val name: String,
    val nationality: String,
    var driver1: Driver,
    var driver2: Driver,
    var points: Int,
    var position: Int,
    var fantasyPrice: Int?

) {
    constructor() : this(
        id = "",
        name = "",
        nationality = "",
        driver1 = Driver(),
        driver2 = Driver(),
        points = 0,
        position = 0,
        fantasyPrice = 0
    )

    class Builder(
        var id: String = "",
        var name: String = "",
        var nationality: String = "",
        var driver1: Driver = Driver(),
        var driver2: Driver = Driver(),
        var points: Int = 0,
        var position: Int = 0,
        var fantasyPrice: Int = 0

    ) {

        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun nationality(nationality: String) = apply { this.nationality = nationality }
        fun driver1(driver1: Driver) = apply { this.driver1 = driver1 }
        fun driver2(driver2: Driver) = apply { this.driver2 = driver2 }
        fun points(points: Int) = apply { this.points = points }
        fun position(position: Int) = apply { this.position = position }
        fun fantasyPrice(fantasyPrice: Int) = apply { this.fantasyPrice = fantasyPrice }
        fun build() = Constructor(
            id,
            name,
            nationality,
            driver1,
            driver2,
            points,
            position,
            fantasyPrice
        )


    }
}