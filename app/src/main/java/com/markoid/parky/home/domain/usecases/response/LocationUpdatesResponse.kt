package com.markoid.parky.home.domain.usecases.response

import com.google.android.gms.maps.model.LatLng

data class LocationUpdatesResponse(
    val distance: String,
    val userLocation: LatLng,
    val time: String,
    val speedKph: String
)
