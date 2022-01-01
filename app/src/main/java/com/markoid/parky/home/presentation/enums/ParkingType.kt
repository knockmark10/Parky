package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingType(@StringRes val typeId: Int) {
    StreetParking(R.string.parking_type_street),
    ParkingLot(R.string.parking_type_lot);

    fun getLocalizedValue(res: Resources): String = res.getString(typeId)

    companion object {
        /**
         * Checks whether or not the provided name exists. The name provided is checked against
         * the enum type, and NOT the localized value.
         */
        fun exists(typeName: String): Boolean =
            values().any { it.name == typeName }

        fun fromLocalizedValue(
            res: Resources,
            value: String
        ): ParkingType? = values().firstOrNull { it.getLocalizedValue(res) == value }

        fun forValue(typeName: String): ParkingType? =
            values().firstOrNull { it.name == typeName }

        fun getLocalizedValues(res: Resources): List<String> =
            values().map { res.getString(it.typeId) }

        fun getIndexForName(name: String): Int {
            val type = values().firstOrNull { it.name == name } ?: return -1
            return values().indexOf(type)
        }
    }
}
