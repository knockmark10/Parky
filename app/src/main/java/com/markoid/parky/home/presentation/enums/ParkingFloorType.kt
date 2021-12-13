package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingFloorType(@StringRes val typeId: Int) {
    Lobby(R.string.parking_floor_type_lobby),
    Floor(R.string.parking_floor_type_floor),
    Roof(R.string.parking_floor_type_roof),
    Basement(R.string.parking_floor_type_basement);

    fun getValue(res: Resources): String = res.getString(typeId)

    companion object {
        fun exists(res: Resources, type: String): Boolean =
            values().any { it.getValue(res) == type }
    }
}
