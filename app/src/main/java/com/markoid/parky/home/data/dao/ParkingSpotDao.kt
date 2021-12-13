package com.markoid.parky.home.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.markoid.parky.core.data.dao.AbstractDao
import com.markoid.parky.home.data.entities.ParkingSpotEntity

@Dao
interface ParkingSpotDao : AbstractDao<ParkingSpotEntity> {

    @Query("SELECT * FROM parking_spot")
    suspend fun getAllParkingSpots(): List<ParkingSpotEntity>?

    @Query("SELECT * FROM parking_spot WHERE parkingType=:type")
    suspend fun getParkingSpotByType(type: String): List<ParkingSpotEntity>?
}
