package com.markoid.parky.settings.domain.usecases

import android.content.res.Resources
import com.markoid.parky.R
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.position.presentation.extensions.isValid
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import com.markoid.parky.settings.domain.responses.ExclusionZoneValidationStatus
import javax.inject.Inject

class ValidateExclusionZoneRequestUseCase
@Inject constructor(
    private val resources: Resources
) : UseCase<ExclusionZoneValidationStatus, ExclusionZoneRequest>() {

    override suspend fun onExecute(
        request: ExclusionZoneRequest
    ): ExclusionZoneValidationStatus = when {
        ParkingColor.exists(resources, request.color).not() ->
            ExclusionZoneValidationStatus.Failure.WrongColor(R.string.invalid_color_message)
        request.location.isValid.not() ->
            ExclusionZoneValidationStatus.Failure.InvalidLocation(R.string.invalid_location_message)
        request.name.length < 4 ->
            ExclusionZoneValidationStatus.Failure.MissingName(R.string.exclusion_zone_invalid_name)
        else -> ExclusionZoneValidationStatus.Success
    }
}
