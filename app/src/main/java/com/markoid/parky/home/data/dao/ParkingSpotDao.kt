package com.markoid.parky.home.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.markoid.parky.core.data.dao.AbstractDao
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus

@Dao
interface ParkingSpotDao : AbstractDao<ParkingSpotEntity> {

    @Query("SELECT * FROM parking_spot")
    suspend fun getAllParkingSpots(): List<ParkingSpotEntity>?

    @Query("SELECT * FROM parking_spot WHERE status=:status")
    suspend fun getParkingSpotByStatus(status: String): List<ParkingSpotEntity>?

    @Query("DELETE FROM parking_spot WHERE id=:id")
    suspend fun deleteParkingSpotById(id: Long)

    @Query("UPDATE parking_spot SET status=:status WHERE id=:parkingId")
    suspend fun updateParkingStatus(parkingId: Long, status: ParkingSpotStatus)
}
