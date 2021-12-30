package com.markoid.parky.home.data.extensions

import android.content.res.Resources
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest

fun ExclusionZoneRequest.toEntity(res: Resources): ExclusionZoneEntity = ExclusionZoneEntity(
    color = ParkingColor.forValue(color, res)!!,
    latitude = latitude,
    longitude = longitude,
    id = id ?: 0L,
    name = name,
    radius = radius
)
