package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingColor(@StringRes val colorId: Int) {
    Yellow(R.string.yellow),
    Red(R.string.red),
    Blue(R.string.blue),
    Green(R.string.green),
    Orange(R.string.orange),
    Brown(R.string.brown),
    Grey(R.string.grey),
    Pink(R.string.pink),
    Black(R.string.black),
    White(R.string.white),
    Purple(R.string.purple);

    fun getValue(res: Resources): String = res.getString(colorId)

    companion object {
        fun exists(res: Resources, type: String): Boolean =
            ParkingFloorType.values().any { it.getValue(res) == type }
    }
}