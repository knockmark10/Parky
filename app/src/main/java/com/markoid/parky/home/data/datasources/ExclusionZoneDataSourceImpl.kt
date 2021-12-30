package com.markoid.parky.home.data.datasources

import com.markoid.parky.home.data.dao.ExclusionZoneDao
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import javax.inject.Inject

class ExclusionZoneDataSourceImpl @Inject constructor(
    private val exclusionZoneDao: ExclusionZoneDao
) : ExclusionZoneDataSource {

    override suspend fun delete(id: Long) = exclusionZoneDao.delete(id)

    override suspend fun getExclusionZones(): List<ExclusionZoneEntity> =
        exclusionZoneDao.getExclusionZones()

    override suspend fun insert(zoneEntity: ExclusionZoneEntity): Long =
        exclusionZoneDao.insert(zoneEntity)

    override suspend fun update(zoneEntity: ExclusionZoneEntity) =
        exclusionZoneDao.update(zoneEntity)
}
