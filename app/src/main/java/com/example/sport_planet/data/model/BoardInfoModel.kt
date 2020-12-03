package com.example.sport_planet.data.model

data class BoardInfo(
    val boardId: Long,
    val title: String,
    val groupStatus: String,
    val exercise: String,
    val city: String,
    val recruitNumber: Int,
    val recruitedNumber: Int,
    val hostId: Long,
    val hostName: String,
    val isBookMark: Boolean,
    val time: String
)