package com.markoid.parky.core.presentation.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.home.data.entities.ParkingSpotEntity

val Location.latLng
    get() = LatLng(latitude, longitude)

val ParkingSpotEntity.latLng
    get() = LatLng(latitude, longitude)
