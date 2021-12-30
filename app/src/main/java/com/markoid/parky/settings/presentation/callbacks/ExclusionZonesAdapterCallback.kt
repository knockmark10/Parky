package com.markoid.parky.settings.presentation.callbacks

import com.markoid.parky.home.data.entities.ExclusionZoneEntity

interface ExclusionZonesAdapterCallback {
    fun onEditExclusionZone(zone: ExclusionZoneEntity)
    fun onDeleteExclusionZone(id: Long)
}
