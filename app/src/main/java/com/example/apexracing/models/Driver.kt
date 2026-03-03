package com.example.apexracing.models

import com.google.firebase.firestore.DocumentReference

data class Driver private constructor(
    val id: String = "",
    val code: String,
    val constructorRef: DocumentReference? = null,
    val givenName: String,
    val familyName: String,
    val permanentNumber: Int,
    val nationality: String,
    val points: Int,
    val position: Int,
    val fantasyPrice: Int?,
    val imgRef: String,
) {
    constructor() : this(
        id = "",
        code = "",
        constructorRef  = null,
        givenName = "",
        familyName = "",
        permanentNumber = 0,
        nationality = "",
        points = 0,
        position = 0,
        fantasyPrice = 0,
        imgRef = ""


        )
    class Builder(
        var id: String = "",
        var code: String = "",
        var constructorRef: DocumentReference? = null,
        var givenName: String = "",
        var familyName: String = "",
        var permanentNumber: Int = 0,
        var nationality: String = "",
        var points: Int = 0,
        var position: Int = 0,
        var fantasyPrice: Int = 0,
        var imgRef: String = ""

    ) {

        fun id(id: String) = apply { this.id = id }
        fun code(code: String) = apply { this.code = code }
        fun constructorRef(constructorRef: DocumentReference) = apply { this.constructorRef = constructorRef }
        fun givenName(givenName: String) = apply { this.givenName = givenName }
        fun familyName(familyName: String) = apply { this.familyName = familyName }
        fun permanentNumber(permanentNumber: Int) = apply { this.permanentNumber = permanentNumber }
        fun nationality(nationality: String) = apply { this.nationality = nationality }
        fun points(points: Int) = apply { this.points = points }
        fun position(position: Int) = apply { this.position = position }
        fun fantasyPrice(fantasyPrice: Int) = apply { this.fantasyPrice = fantasyPrice }
        fun imgRef(imgRef: String) = apply { this.imgRef = imgRef }

        fun build() = Driver(
        id,
        code,
        constructorRef,
        givenName,
        familyName,
        permanentNumber,
        nationality,
        points,
        position,
        fantasyPrice,
        imgRef
        )


    }

}
