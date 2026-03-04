package com.example.apexracing.models

import com.google.firebase.firestore.DocumentReference

data class Driver (
    val id: String = "",
    val code: String,
    val constructor: DocumentReference? = null,
    val givenName: String,
    val familyName: String,
    val permanentNumber: Int,
    val nationality: String,
    val points: Int,
    val position: Int,
    val fantasyPrice: Float,
    val imgRef: String,
) {
    fun getFullName(): String {
        return buildString {
            append(givenName)
                .append(" ")
                .append(familyName)
        }
    }

    class Builder(
        var id: String = "",
        var code: String = "",
        var constructor: DocumentReference? = null,
        var givenName: String = "",
        var familyName: String = "",
        var permanentNumber: Int = 0,
        var nationality: String = "",
        var points: Int = 0,
        var position: Int = 0,
        var fantasyPrice: Float = 0F,
        var imgRef: String = ""

    ) {

        fun id(id: String) = apply { this.id = id }
        fun code(code: String) = apply { this.code = code }
        fun constructor(constructorRef: DocumentReference) = apply { this.constructor = constructor }
        fun givenName(givenName: String) = apply { this.givenName = givenName }
        fun familyName(familyName: String) = apply { this.familyName = familyName }
        fun permanentNumber(permanentNumber: Int) = apply { this.permanentNumber = permanentNumber }
        fun nationality(nationality: String) = apply { this.nationality = nationality }
        fun points(points: Int) = apply { this.points = points }
        fun position(position: Int) = apply { this.position = position }
        fun fantasyPrice(fantasyPrice: Float) = apply { this.fantasyPrice = fantasyPrice }
        fun imgRef(imgRef: String) = apply { this.imgRef = imgRef }

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
        fantasyPrice,
        imgRef
        )


    }

}
