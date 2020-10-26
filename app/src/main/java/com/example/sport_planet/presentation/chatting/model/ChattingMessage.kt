package com.example.sport_planet.presentation.chatting.model

import com.beust.klaxon.Json

data class ChattingMessage(
        @Json(name = "createdAt")
        var timestamp: String,
        @Json(name = "senderId")
        val senderId: Int,
        @Json(name = "content")
        val content: String,
        @Json(name = "type")
        val type: String,
        @Json(name = "read")
        val isRead: Boolean
)