package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.repositories.ParkingRepository
import javax.inject.Inject

class GetParkingHistoryUseCase
@Inject constructor(
    private val parkingRepository: ParkingRepository
) : UseCase<List<ParkingSpotEntity>, Unit>() {

    override suspend fun onExecute(request: Unit): List<ParkingSpotEntity> =
        parkingRepository.getAllParkingSpots()
}
