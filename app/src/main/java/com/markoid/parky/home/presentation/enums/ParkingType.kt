package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingType(@StringRes val typeId: Int) {
    StreetParking(R.string.parking_type_street),
    ParkingLot(R.string.parking_type_lot);

    fun getValue(res: Resources): String = res.getString(typeId)

    companion object {
        fun exists(res: Resources, type: String): Boolean =
            values().any { it.getValue(res) == type }

        fun forValue(res: Resources, value: String): ParkingType? =
            values().firstOrNull { it.getValue(res) == value }
    }
}
