package com.example.apexracing.models

import com.google.firebase.firestore.DocumentReference

data class Constructor(
    val id: String = "",
    val name: String,
    val nationality: String,
    val driver1: DocumentReference? = null,
    val driver2: DocumentReference? = null,
    val points: Int,
    val position: Int,
    val fantasyPrice: Float,
    val imgRef: String


) {

    class Builder(
        var id: String = "",
        var name: String = "",
        var nationality: String = "",
        var driver1: DocumentReference? = null,
        var driver2: DocumentReference? = null,
        var points: Int = 0,
        var position: Int = 0,
        var fantasyPrice: Float = 0F,
        var imgRef: String = ""


    ) {

        fun id(id: String) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun nationality(nationality: String) = apply { this.nationality = nationality }
        fun driver1(driver1: DocumentReference) = apply { this.driver1 = driver1 }
        fun driver2(driver2: DocumentReference) = apply { this.driver2 = driver2 }
        fun points(points: Int) = apply { this.points = points }
        fun position(position: Int) = apply { this.position = position }
        fun fantasyPrice(fantasyPrice: Float) = apply { this.fantasyPrice = fantasyPrice }
        fun imgRef(imgRef: String) = apply { this.imgRef = imgRef }
        fun build() = Constructor(
            id,
            name,
            nationality,
            driver1,
            driver2,
            points,
            position,
            fantasyPrice,
            imgRef
        )


    }
}