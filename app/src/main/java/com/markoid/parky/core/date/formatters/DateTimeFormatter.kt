package com.markoid.parky.core.date.formatters

import com.markoid.parky.core.date.enums.FormatType
import com.markoid.parky.core.date.extensions.capitalizeWords
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder

object DateTimeFormatter {

    fun format(type: FormatType, dateTime: DateTime): String {
        val formatter = DateTimeFormat.forPattern(type.pattern)
        DateTimeFormatterBuilder()
            .appendMonthOfYearText()
            .toFormatter()
        return dateTime.toString(formatter).capitalizeWords()
    }
}
