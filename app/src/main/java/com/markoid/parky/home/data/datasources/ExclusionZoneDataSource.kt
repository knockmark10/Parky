package com.markoid.parky.home.data.datasources

import com.markoid.parky.home.data.entities.ExclusionZoneEntity

interface ExclusionZoneDataSource {
    suspend fun delete(id: Long)
    suspend fun getExclusionZones(): List<ExclusionZoneEntity>
    suspend fun insert(zoneEntity: ExclusionZoneEntity): Long
    suspend fun update(zoneEntity: ExclusionZoneEntity)
}
