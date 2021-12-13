package com.markoid.parky.home.data.datasources

import com.markoid.parky.home.data.dao.ParkingSpotDao
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import javax.inject.Inject

class ParkingDataSourceImpl
@Inject constructor(
    private val parkingSpotDao: ParkingSpotDao
) : ParkingDataSource {

    override suspend fun getActiveParkingSpot(): ParkingSpotEntity = this.parkingSpotDao
        .getParkingSpotByType(ParkingSpotStatus.Active.name)
        ?.firstOrNull() ?: throw IllegalStateException("No parking spot saved into database")

    override suspend fun getAllParkingSpots(): List<ParkingSpotEntity> = this.parkingSpotDao
        .getAllParkingSpots() ?: emptyList()

    override suspend fun getArchivedParkingSpots(): List<ParkingSpotEntity> = this.parkingSpotDao
        .getParkingSpotByType(ParkingSpotStatus.Archived.name) ?: emptyList()

    override suspend fun saveParkingSpotIntoDatabase(parkingSpot: ParkingSpotEntity) {
        this.parkingSpotDao.insert(parkingSpot)
    }

    override suspend fun updateParkingSpot(parkingSpot: ParkingSpotEntity) {
        this.parkingSpotDao.update(parkingSpot)
    }
}
