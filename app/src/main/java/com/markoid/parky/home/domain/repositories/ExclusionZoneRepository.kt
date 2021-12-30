package com.markoid.parky.home.domain.repositories

import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest

interface ExclusionZoneRepository {
    suspend fun delete(id: Long)
    suspend fun getExclusionZones(): List<ExclusionZoneEntity>
    suspend fun insert(zone: ExclusionZoneRequest): Long
    suspend fun update(zone: ExclusionZoneRequest)
}
