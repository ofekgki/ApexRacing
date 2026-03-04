package com.example.apexracing.models

data class FantasyItem(
    val id: String,
    val name: String,
    val points: Int,
    val price: Float,
    val imageUrl: String,
    val type: UserViewModel.PickType
)