package com.markoid.parky.position.presentation.extensions

import android.location.Location
import com.google.android.gms.maps.model.LatLng

val Double.isValid: Boolean
    get() = this != 0.0

val Location.isValid: Boolean
    get() = latitude.isValid && longitude.isValid

val LatLng.isValid: Boolean
    get() = latitude.isValid && longitude.isValid
