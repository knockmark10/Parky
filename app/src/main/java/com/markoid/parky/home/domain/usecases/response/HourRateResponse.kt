package com.markoid.parky.home.domain.usecases.response

import com.google.android.gms.maps.model.LatLng

data class HourRateResponse(
    val address: String,
    val floor: String,
    val hourRate: String,
    val parkingLocation: LatLng,
    val parkingLotIdentifier: String,
    val parkedDuration: String,
    val total: String
)
