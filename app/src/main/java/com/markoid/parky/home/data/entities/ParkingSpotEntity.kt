package com.markoid.parky.home.data.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import org.joda.time.DateTime

@Entity(tableName = "parking_spot")
data class ParkingSpotEntity(

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "color")
    val color: String,

    @ColumnInfo(name = "fare")
    val fare: Double,

    @ColumnInfo(name = "floorNumber")
    val floorNumber: String,

    @ColumnInfo(name = "floorType")
    val floorType: ParkingFloorType?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "lotIdentifier")
    val lotIdentifier: String,

    @ColumnInfo(name = "parkingTime")
    val parkingTime: DateTime,

    @ColumnInfo(name = "parkingType")
    val parkingType: ParkingType,

    @ColumnInfo(name = "photo")
    val photo: Uri?,

    @ColumnInfo(name = "status")
    val status: ParkingSpotStatus
)
