package com.markoid.parky.position.data.entities

import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.position.data.enums.Country
import com.markoid.parky.position.data.enums.CountryState

data class PositionEntity(
    val city: String = "",
    val country: Country = Country.Mexico,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val state: CountryState = CountryState.MEX_DF,
    val streetAddress: String = ""
) {

    val latLng: LatLng
        get() = LatLng(latitude, longitude)
}
