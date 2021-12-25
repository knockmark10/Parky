package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.repositories.ParkingRepository
import javax.inject.Inject

class FinishParkingUseCase
@Inject constructor(
    private val parkingRepository: ParkingRepository
) : UseCase<Unit, Long>() {

    override suspend fun onExecute(request: Long) {
        parkingRepository.finishParking(request)
    }
}
