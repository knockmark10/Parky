package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingFloorType(@StringRes val typeId: Int) {
    Lobby(R.string.parking_floor_type_lobby),
    Floor(R.string.parking_floor_type_floor),
    Roof(R.string.parking_floor_type_roof),
    Basement(R.string.parking_floor_type_basement);

    fun getLocalizedValue(res: Resources): String = res.getString(typeId)

    companion object {
        /**
         * Checks whether or not the provided name exists. The name provided is checked against
         * the enum type, and NOT the localized value.
         */
        fun exists(typeName: String): Boolean =
            values().any { it.name == typeName }

        fun forValue(typeName: String): ParkingFloorType? =
            values().firstOrNull { it.name == typeName }

        fun fromLocalizedValue(res: Resources, value: String): ParkingFloorType? =
            values().firstOrNull { it.getLocalizedValue(res) == value }
    }
}
