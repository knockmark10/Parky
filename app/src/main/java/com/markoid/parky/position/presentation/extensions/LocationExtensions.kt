package com.markoid.parky.position.presentation.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

val Location.isValid: Boolean
    get() = latitude != 0.0 && longitude != 0.0

val LatLng.isValid: Boolean
    get() = latitude != 0.0 && longitude != 0.0
