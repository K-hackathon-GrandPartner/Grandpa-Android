package com.example.grandpa

data class room_data(
    val id: Int,
    val imageUrl: String,
    val buildingType: String,
    val roomSizeType: String,
    val roomSize: Float,
    val roomFloor: Int,
    val deposit: Int,
    val monthlyRent: Int,
    val address: String,
    val title: String,
    val postDate: String,
)
