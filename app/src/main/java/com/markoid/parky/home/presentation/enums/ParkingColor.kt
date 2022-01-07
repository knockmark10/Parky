package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingColor(
    @StringRes val colorNameId: Int,
    @ColorRes val transparentColorId: Int,
    @DrawableRes val marker: Int
) {
    Yellow(R.string.yellow, R.color.md_yellow_500_50, R.drawable.ic_exclusion_yellow),
    Red(R.string.red, R.color.md_red_500_50, R.drawable.ic_exclusion_red),
    Blue(R.string.blue, R.color.md_blue_500_50, R.drawable.ic_exclusion_blue),
    Green(R.string.green, R.color.md_green_500_50, R.drawable.ic_exclusion_green),
    Orange(R.string.orange, R.color.md_orange_500_50, R.drawable.ic_exclusion_orange),
    Brown(R.string.brown, R.color.md_brown_500_50, R.drawable.ic_exclusion_brown),
    Grey(R.string.grey, R.color.md_grey_500_50, R.drawable.ic_exclusion_grey),
    Pink(R.string.pink, R.color.md_pink_500_50, R.drawable.ic_exclusion_pink),
    Black(R.string.black, R.color.md_black_1000_50, R.drawable.ic_exclusion_black),
    White(R.string.white, R.color.md_white_1000_50, R.drawable.ic_exclusion_white),
    Purple(R.string.purple, R.color.md_purple_500_50, R.drawable.ic_exclusion_purple);

    fun getLocalizedValue(res: Resources): String = res.getString(colorNameId)

    companion object {
        /**
         * Checks whether or not the provided name exists. The name provided is checked against
         * the enum type, and NOT the localized value.
         */
        fun exists(typeName: String): Boolean =
            values().any { it.name == typeName }

        fun forValue(typeName: String): ParkingColor? =
            values().firstOrNull { it.name == typeName }

        fun fromLocalizedValue(res: Resources, value: String): ParkingColor? =
            values().firstOrNull { it.getLocalizedValue(res) == value }
    }
}
