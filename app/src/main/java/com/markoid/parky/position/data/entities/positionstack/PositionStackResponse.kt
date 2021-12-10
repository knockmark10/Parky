package com.markoid.parky.position.data.entities.positionstack

import com.google.gson.annotations.SerializedName

data class PositionStackResponse(
    @SerializedName("data")
    private val _data: List<PositionStackData>? = null
) {
    val data: List<PositionStackData>
        get() = _data ?: emptyList()
}
