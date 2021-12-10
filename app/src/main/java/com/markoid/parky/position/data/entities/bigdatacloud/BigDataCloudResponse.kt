package com.markoid.parky.position.data.entities.bigdatacloud

import com.google.gson.annotations.SerializedName

data class BigDataCloudResponse(

    @field:SerializedName("continent")
    private val _continent: String? = null,

    @field:SerializedName("city")
    private val _city: String? = null,

    @field:SerializedName("latitude")
    private val _latitude: Double? = null,

    @field:SerializedName("plusCode")
    private val _plusCode: String? = null,

    @field:SerializedName("locality")
    private val _locality: String? = null,

    @field:SerializedName("postcode")
    private val _postcode: String? = null,

    @field:SerializedName("localityInfo")
    private val _localityInfo: LocalityInfo? = null,

    @field:SerializedName("principalSubdivisionCode")
    private val _principalSubdivisionCode: String? = null,

    @field:SerializedName("principalSubdivision")
    private val _principalSubdivision: String? = null,

    @field:SerializedName("countryCode")
    private val _countryCode: String? = null,

    @field:SerializedName("countryName")
    private val _countryName: String? = null,

    @field:SerializedName("longitude")
    private val _longitude: Double? = null,

    @field:SerializedName("localityLanguageRequested")
    private val _localityLanguageRequested: String? = null,

    @field:SerializedName("continentCode")
    private val _continentCode: String? = null
) {

    val city: String
        get() = _city ?: ""

    val principalSubdivision: String
        get() = _principalSubdivision ?: ""

    val principalSubdivisionCode: String
        get() = _principalSubdivisionCode ?: ""

    val latitude: Double
        get() = _latitude ?: 0.0

    val longitude: Double
        get() = _longitude ?: 0.0

    val locality: String
        get() = _locality ?: ""

    val cityName: String
        get() = if (city.isNotEmpty()) city else locality
}
