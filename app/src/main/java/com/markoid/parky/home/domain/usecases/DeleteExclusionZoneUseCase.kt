package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.repositories.ExclusionZoneRepository
import javax.inject.Inject

class DeleteExclusionZoneUseCase
@Inject constructor(
    private val exclusionZoneRepository: ExclusionZoneRepository
) : UseCase<Unit, Long>() {

    override suspend fun onExecute(request: Long) {
        exclusionZoneRepository.delete(request)
    }
}
