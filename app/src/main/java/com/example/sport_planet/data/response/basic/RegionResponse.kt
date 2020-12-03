package com.example.sport_planet.data.response.basic


import com.google.gson.annotations.SerializedName

data class RegionResponse(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("type")
    val type: String
) {
    data class Data(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )

    fun isSuccess(): Boolean {
        return status == 200
    }
}