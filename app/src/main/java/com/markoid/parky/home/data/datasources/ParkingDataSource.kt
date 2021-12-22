package com.markoid.parky.home.data.datasources

import com.markoid.parky.home.data.entities.ParkingSpotEntity

interface ParkingDataSource {
    suspend fun deleteParkingSpot(parkingSpotId: Long)
    suspend fun getActiveParkingSpot(): ParkingSpotEntity
    suspend fun getAllParkingSpots(): List<ParkingSpotEntity>
    suspend fun getArchivedParkingSpots(): List<ParkingSpotEntity>
    suspend fun saveParkingSpotIntoDatabase(parkingSpot: ParkingSpotEntity): Long
    suspend fun updateParkingSpot(parkingSpot: ParkingSpotEntity)
}
