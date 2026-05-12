package com.example.apexracing.models

import com.example.apexracing.models.User.UserViewModel

data class FantasyItem(
    val id: String,
    val name: String,
    val points: Int,
    val price: Float,
    val imageUrl: String,
    val type: UserViewModel.PickType
)