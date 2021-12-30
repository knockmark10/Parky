package com.markoid.parky.home.data.mappers

import android.content.res.Resources
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.data.extensions.toEntity
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import javax.inject.Inject

class ExclusionZoneMapper @Inject constructor(private val res: Resources) {

    fun mapFromRequestToEntity(
        zone: ExclusionZoneRequest
    ): ExclusionZoneEntity = zone.toEntity(res)
}
