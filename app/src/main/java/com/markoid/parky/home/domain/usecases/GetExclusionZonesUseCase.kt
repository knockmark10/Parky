package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.domain.repositories.ExclusionZoneRepository
import javax.inject.Inject

class GetExclusionZonesUseCase
@Inject constructor(
    private val exclusionZoneRepository: ExclusionZoneRepository
) : UseCase<List<ExclusionZoneEntity>, Unit>() {

    override suspend fun onExecute(request: Unit): List<ExclusionZoneEntity> =
        exclusionZoneRepository.getExclusionZones()
}
