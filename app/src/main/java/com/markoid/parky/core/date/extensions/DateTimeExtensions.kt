package com.markoid.parky.core.date.extensions

import com.markoid.parky.core.date.enums.FormatType
import com.markoid.parky.core.date.formatters.DateTimeFormatter
import org.joda.time.DateTime
import java.util.*

fun String.capitalizeWords(locale: Locale = Locale.getDefault()): String =
    split(" ").joinToString(" ") { it.capitalize(locale) }

fun DateTime.formatWith(type: FormatType): String =
    DateTimeFormatter.format(type, this)
