package com.example.sport_planet.model

data class MyViewHistoryModel(
    val isHost : Boolean,
    val userName : String,
    val isContinue : Boolean,
    val leftTime : String,
    val boardInfo : BoardInfo
)