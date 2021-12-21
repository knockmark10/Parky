package com.markoid.parky.home.domain.repositories

import com.markoid.parky.home.data.datasources.ParkingDataSource
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.data.mappers.ParkingSpotMapper
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import javax.inject.Inject

class ParkingRepositoryImpl
@Inject constructor(
    private val parkingDataSource: ParkingDataSource,
    private val parkingSpotMapper: ParkingSpotMapper
) : ParkingRepository {

    override suspend fun archiveParkingSpot(parkingSpot: ParkingSpotEntity) {
        val archivedParkingSpot = parkingSpot.copy(status = ParkingSpotStatus.Archived)
        this.parkingDataSource.updateParkingSpot(archivedParkingSpot)
    }

    override suspend fun deleteParkingSpot(spotId: Long) {
        parkingDataSource.deleteParkingSpot(spotId)
    }

    override suspend fun getActiveParkingSpot(): ParkingSpotEntity =
        this.parkingDataSource.getActiveParkingSpot()

    override suspend fun getAllParkingSpots(): List<ParkingSpotEntity> =
        this.parkingDataSource.getAllParkingSpots()

    override suspend fun getArchivedParkingSpots(): List<ParkingSpotEntity> =
        this.parkingDataSource.getArchivedParkingSpots()

    override suspend fun saveParkingSpotIntoDatabase(parkingSpot: ParkingSpotRequest) {
        this.parkingDataSource.saveParkingSpotIntoDatabase(
            this.parkingSpotMapper.mapFromRequestToEntity(parkingSpot, ParkingSpotStatus.Active)
        )
    }
}
