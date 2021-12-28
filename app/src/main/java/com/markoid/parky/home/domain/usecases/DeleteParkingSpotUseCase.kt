package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import javax.inject.Inject

class DeleteParkingSpotUseCase
@Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val parkingRepository: ParkingRepository
) : UseCase<Unit, Long>() {

    override suspend fun onExecute(request: Long) {
        devicePreferences.isParkingSpotActive = false
        parkingRepository.deleteParkingSpot(request)
    }
}
