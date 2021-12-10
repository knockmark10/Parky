package com.markoid.parky.position.data.entities.positionstack

import com.google.gson.annotations.SerializedName

data class PositionStackErrorResponse(
    @SerializedName("code")
    val code: String = "Error",
    @SerializedName("message")
    val message: String = "There has been an error"
)
