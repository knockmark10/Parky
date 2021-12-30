package com.markoid.parky.home.domain.usecases

import android.location.Location
import com.markoid.parky.position.data.repositories.TrackingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRealTimeLocationUseCase @Inject constructor(
    private val trackingRepository: TrackingRepository
) {

    fun getLocationUpdates(): Flow<Location> = trackingRepository.getRealTimeLocation()
}
