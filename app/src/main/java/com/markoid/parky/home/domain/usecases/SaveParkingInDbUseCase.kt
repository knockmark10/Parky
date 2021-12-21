package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import javax.inject.Inject

class SaveParkingInDbUseCase
@Inject constructor(
    private val parkingRepository: ParkingRepository
) : UseCase<Unit, ParkingSpotRequest>() {

    override suspend fun onExecute(request: ParkingSpotRequest) {
        parkingRepository.saveParkingSpotIntoDatabase(request)
    }
}
