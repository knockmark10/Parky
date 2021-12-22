package com.markoid.parky.core.presentation.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

val Location.latLng
    get() = LatLng(latitude, longitude)
