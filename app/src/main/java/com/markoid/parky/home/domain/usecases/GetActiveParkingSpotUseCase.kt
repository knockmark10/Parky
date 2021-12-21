package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.repositories.ParkingRepository
import javax.inject.Inject

class GetActiveParkingSpotUseCase
@Inject constructor(
    private val parkingRepository: ParkingRepository
) : UseCase<ParkingSpotEntity, Unit>() {

    override suspend fun onExecute(request: Unit): ParkingSpotEntity =
        parkingRepository.getActiveParkingSpot()
}
