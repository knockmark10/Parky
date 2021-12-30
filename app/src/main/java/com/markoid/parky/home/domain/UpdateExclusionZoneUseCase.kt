package com.markoid.parky.home.domain

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.repositories.ExclusionZoneRepository
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import com.markoid.parky.settings.domain.responses.ExclusionZoneValidationStatus
import com.markoid.parky.settings.domain.usecases.ValidateExclusionZoneRequestUseCase
import javax.inject.Inject

class UpdateExclusionZoneUseCase
@Inject constructor(
    private val exclusionZoneRepository: ExclusionZoneRepository,
    private val validateExclusionZoneUseCase: ValidateExclusionZoneRequestUseCase
) : UseCase<ExclusionZoneValidationStatus, ExclusionZoneRequest>() {

    override suspend fun onExecute(request: ExclusionZoneRequest): ExclusionZoneValidationStatus {
        // Validate incoming request
        val status = validateExclusionZoneUseCase.onExecute(request)
        // If status is not success, return it to handle the view
        if (status !is ExclusionZoneValidationStatus.Success) return status
        // Save it in database
        exclusionZoneRepository.update(request)
        // Return success
        return ExclusionZoneValidationStatus.Success
    }
}
