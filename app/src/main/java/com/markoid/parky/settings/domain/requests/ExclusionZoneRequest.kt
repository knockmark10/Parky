package com.markoid.parky.settings.domain.requests

import com.google.android.gms.maps.model.LatLng

data class ExclusionZoneRequest(
    val color: String,
    val latitude: Double,
    val longitude: Double,
    val id: Long? = null,
    val name: String,
    val radius: Double
) {

    val location: LatLng
        get() = LatLng(latitude, longitude)
}
