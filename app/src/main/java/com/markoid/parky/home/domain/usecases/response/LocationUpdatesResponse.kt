package com.markoid.parky.home.domain.usecases.response

import com.google.android.gms.maps.model.LatLng

data class LocationUpdatesResponse(
    val distance: Double,
    val distanceWithDisplayFormat: String,
    val userLocation: LatLng,
    val time: String,
    val speedKph: String
)
