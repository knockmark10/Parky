package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import javax.inject.Inject

class ArchiveParkingSpotUseCase
@Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val parkingRepository: ParkingRepository
) : UseCase<Unit, ParkingSpotEntity>() {

    override suspend fun onExecute(request: ParkingSpotEntity) {
        devicePreferences.isParkingSpotActive = false
        parkingRepository.archiveParkingSpot(request)
    }
}
