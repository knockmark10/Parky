package com.markoid.parky.core.presentation.enums

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.markoid.parky.R

enum class AlertType(
    @DrawableRes val iconId: Int,
    @ColorRes val colorId: Int,
    @ColorRes val darkColorId: Int,
    @StringRes val titleId: Int
) {
    Success(R.drawable.ic_success, R.color.md_green_600, R.color.md_green_900, R.string.success),
    Error(R.drawable.ic_error, R.color.md_red_600, R.color.md_red_900, R.string.error),
    Info(R.drawable.ic_info, R.color.md_blue_600, R.color.md_blue_900, R.string.info),
    Warning(R.drawable.ic_warning, R.color.md_yellow_700, R.color.md_yellow_900, R.string.warning)
}
