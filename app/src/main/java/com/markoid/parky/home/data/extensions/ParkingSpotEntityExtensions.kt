package com.markoid.parky.home.data.extensions

import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.date.enums.FormatType
import com.markoid.parky.core.date.formatters.DateTimeFormatter
import com.markoid.parky.home.data.entities.ParkingSpotEntity

val ParkingSpotEntity.latLng
    get() = LatLng(latitude, longitude)

fun ParkingSpotEntity.getAlarmTimeFormatted(): String =
    alarmTime?.let { DateTimeFormatter.format(FormatType.MONTH_DAY_YEAR_HOUR, it) } ?: ""
