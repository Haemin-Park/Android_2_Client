package com.example.sport_planet.data.request.board

data class ReportRequest(
    val boardId: Long,
    val reportType: Long,
    val content: String
)