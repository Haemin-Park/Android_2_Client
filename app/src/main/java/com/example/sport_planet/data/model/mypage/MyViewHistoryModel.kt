package com.example.sport_planet.data.model.mypage

import com.example.sport_planet.data.model.BoardInfo

data class MyViewHistoryModel(
    val isHost : Boolean,
    val userName : String,
    val isContinue : Boolean,
    val boardInfo : BoardInfo
)