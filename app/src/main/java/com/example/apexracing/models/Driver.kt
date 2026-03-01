package com.example.apexracing.models

import com.example.apexracing.models.Constructor

data class Driver private constructor(
    val id: String = "",
    val code: String,
    val constructor: Constructor,
    val givenName: String,
    val familyName: String,
    val permanentNumber: Int,
    val nationality: String,
    var points: Int,
    var position: Int,
    var fantasyPrice: Int?
) {
    constructor() : this(
        id = "",
        code = "",
        constructor = Constructor(),
        givenName = "",
        familyName = "",
        permanentNumber = 0,
        nationality = "",
        points = 0,
        position = 0,
        fantasyPrice = 0,


        )

    class Builder(
        var id: String = "",
        var code: String = "",
        var constructor: Constructor = Constructor(),
        var givenName: String = "",
        var familyName: String = "",
        var permanentNumber: Int = 0,
        var nationality: String = "",
        var points: Int = 0,
        var position: Int = 0,
        var fantasyPrice: Int = 0

    ) {

        fun id(id: String) = apply { this.id = id }
        fun code(code: String) = apply { this.code = code }
        fun constructor(constructor: Constructor) = apply { this.constructor = constructor }
        fun givenName(givenName: String) = apply { this.givenName = givenName }
        fun familyName(familyName: String) = apply { this.familyName = familyName }
        fun permanentNumber(permanentNumber: Int) = apply { this.permanentNumber = permanentNumber }
        fun nationality(nationality: String) = apply { this.nationality = nationality }
        fun points(points: Int) = apply { this.points = points }
        fun position(position: Int) = apply { this.position = position }
        fun fantasyPrice(fantasyPrice: Int) = apply { this.fantasyPrice = fantasyPrice }

        fun build() = Driver(
        id,
        code,
        constructor,
        givenName,
        familyName,
        permanentNumber,
        nationality,
        points,
        position,
        fantasyPrice
        )


    }
}
