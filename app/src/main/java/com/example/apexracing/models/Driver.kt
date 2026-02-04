package com.example.apexracing.models

data class Driver (
    val fullName: String,
    val givenName: String,
    val familyName: String,
    val permanentNumber: String?,
    val nationality: String?
)