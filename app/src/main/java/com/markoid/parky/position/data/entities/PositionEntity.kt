package com.markoid.parky.position.data.entities

import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.date.enums.FormatType
import com.markoid.parky.core.date.extensions.formatWith
import com.markoid.parky.position.data.enums.Country
import com.markoid.parky.position.data.enums.CountryState
import org.joda.time.DateTime

data class PositionEntity(
    val city: String = "",
    val country: Country = Country.Mexico,
    val dateTime: DateTime,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val state: CountryState = CountryState.MEX_DF,
    val streetAddress: String = ""
) {

    val latLng: LatLng
        get() = LatLng(latitude, longitude)

    val dateFormatted: String
        get() = dateTime.formatWith(FormatType.MONTH_DAY_YEAR_HOUR)
}
