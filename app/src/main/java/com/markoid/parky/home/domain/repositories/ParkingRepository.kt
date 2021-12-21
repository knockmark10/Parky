package com.markoid.parky.home.domain.repositories

import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest

interface ParkingRepository {
    suspend fun deleteParkingSpot(spotId: Long)
    suspend fun getActiveParkingSpot(): ParkingSpotEntity
    suspend fun getAllParkingSpots(): List<ParkingSpotEntity>
    suspend fun getArchivedParkingSpots(): List<ParkingSpotEntity>
    suspend fun saveParkingSpotIntoDatabase(parkingSpot: ParkingSpotRequest)
    suspend fun archiveParkingSpot(parkingSpot: ParkingSpotEntity)
}
