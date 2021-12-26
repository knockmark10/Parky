package com.markoid.parky.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.markoid.parky.core.data.converters.DatabaseConverters
import com.markoid.parky.home.data.dao.ParkingSpotDao
import com.markoid.parky.home.data.entities.ParkingSpotEntity

@Database(
    entities = [ParkingSpotEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class ParkyDatabase : RoomDatabase() {
    abstract fun parkingSpotDao(): ParkingSpotDao
}
