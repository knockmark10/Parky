package com.markoid.parky.position.data.entities.positionstack

import com.google.gson.annotations.SerializedName

data class PositionStackData(

    @SerializedName("country")
    val country: String = "",

    @SerializedName("distance")
    val distance: Double = 0.0,

    @SerializedName("latitude")
    val latitude: Double = 0.0,

    @SerializedName("locality")
    private val _locality: String? = null,

    @SerializedName("label")
    val fullAddress: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("number")
    val number: String = "",

    @SerializedName("country_code")
    val countryCode: String = "",

    @SerializedName("street")
    val street: String = "",

    @SerializedName("neighbourhood")
    val neighbourhood: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("postal_code")
    val postalCode: String = "",

    @SerializedName("region")
    val region: String = "",

    @SerializedName("longitude")
    val longitude: Double = 0.0,

    @SerializedName("region_code")
    val regionCode: String = ""
) {

    val locality: String
        get() = _locality ?: ""
}
