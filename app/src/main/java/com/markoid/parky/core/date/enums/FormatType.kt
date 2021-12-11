package com.markoid.parky.core.date.enums

enum class FormatType(val pattern: String) {
    MONTH_DAY_YEAR("MMMM dd yyyy"),
    WEEK_MONTH_DAY_HOUR("EEEE MMMM dd HH:mm"),
    WEEK_MONTH_DAY_YEAR_HOUR("EEEE MMMM dd y HH:mm")
}
