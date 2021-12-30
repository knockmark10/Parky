package com.markoid.parky.home.domain.repositories

import com.markoid.parky.home.data.datasources.ExclusionZoneDataSource
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.data.mappers.ExclusionZoneMapper
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import javax.inject.Inject

class ExclusionZoneRepositoryImpl
@Inject constructor(
    private val exclusionZoneDataSource: ExclusionZoneDataSource,
    private val mapper: ExclusionZoneMapper
) : ExclusionZoneRepository {

    override suspend fun delete(id: Long) =
        exclusionZoneDataSource.delete(id)

    override suspend fun getExclusionZones(): List<ExclusionZoneEntity> =
        exclusionZoneDataSource.getExclusionZones()
            .sortedBy { it.name }

    override suspend fun insert(zone: ExclusionZoneRequest): Long =
        exclusionZoneDataSource.insert(mapper.mapFromRequestToEntity(zone))

    override suspend fun update(zone: ExclusionZoneRequest) =
        exclusionZoneDataSource.update(mapper.mapFromRequestToEntity(zone))
}
