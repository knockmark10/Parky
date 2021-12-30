package com.markoid.parky.home.presentation.enums

import android.content.res.Resources
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class ParkingColor(
    @StringRes val colorNameId: Int,
    @ColorRes val solidColorId: Int,
    @ColorRes val transparentColorId: Int
) {
    Yellow(R.string.yellow, R.color.md_yellow_700, R.color.md_yellow_500_50),
    Red(R.string.red, R.color.md_red_700, R.color.md_red_500_50),
    Blue(R.string.blue, R.color.md_blue_700, R.color.md_blue_500_50),
    Green(R.string.green, R.color.md_green_700, R.color.md_green_500_50),
    Orange(R.string.orange, R.color.md_orange_700, R.color.md_orange_500_50),
    Brown(R.string.brown, R.color.md_brown_700, R.color.md_brown_500_50),
    Grey(R.string.grey, R.color.md_grey_700, R.color.md_grey_500_50),
    Pink(R.string.pink, R.color.md_pink_500, R.color.md_pink_500_50),
    Black(R.string.black, R.color.md_black_1000, R.color.md_black_1000_50),
    White(R.string.white, R.color.md_white_1000, R.color.md_white_1000_50),
    Purple(R.string.purple, R.color.md_purple_700, R.color.md_purple_500_50);

    fun getValue(res: Resources): String = res.getString(colorNameId)

    companion object {
        fun exists(res: Resources, colorValue: String): Boolean =
            values().any { it.getValue(res) == colorValue }

        fun forValue(value: String, res: Resources): ParkingColor? =
            values().firstOrNull { it.getValue(res) == value }
    }
}
