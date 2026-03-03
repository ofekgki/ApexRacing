package com.example.apexracing.models

import com.google.firebase.firestore.DocumentReference

data class Constructor private constructor(
    val id: String = "",
    val name: String,
    val nationality: String,
    val driver1Ref: DocumentReference? = null,
    val driver2Ref: DocumentReference? = null,
    val points: Int,
    val position: Int,
    val fantasyPrice: Int?,
    val imgRef: String


) {
    constructor() : this(
        id = "",
        name = "",
        nationality = "",
        driver1Ref = null,
        driver2Ref = null,
        points = 0,
        position = 0,
        fantasyPrice = 0,
        imgRef = ""


    )

    class Builder(
        var id: String = "",
        var name: String = "",
        var nationality: String = "",
        var driver1Ref: DocumentReference? = null,
        var driver2Ref: DocumentReference? = null,
        var points: Int = 0,
        var position: Int = 0,
        var fantasyPrice: Int = 0,
        var imgRef: String = ""


    ) {

        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun nationality(nationality: String) = apply { this.nationality = nationality }
        fun driver1Ref(driver1Ref: DocumentReference) = apply { this.driver1Ref = driver1Ref }
        fun driver2Ref(driver2Ref: DocumentReference) = apply { this.driver2Ref = driver2Ref }
        fun points(points: Int) = apply { this.points = points }
        fun position(position: Int) = apply { this.position = position }
        fun fantasyPrice(fantasyPrice: Int) = apply { this.fantasyPrice = fantasyPrice }
        fun imgRef(imgRef: String) = apply { this.imgRef = imgRef }
        fun build() = Constructor(
            id,
            name,
            nationality,
            driver1Ref,
            driver2Ref,
            points,
            position,
            fantasyPrice,
            imgRef
        )


    }
}