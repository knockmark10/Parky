package com.markoid.parky.home.data.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import org.joda.time.DateTime
import java.io.Serializable

@Entity(tableName = "parking_spot")
data class ParkingSpotEntity(

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "alarm_time")
    val alarmTime: DateTime?,

    @ColumnInfo(name = "color")
    val color: String,

    @ColumnInfo(name = "hour_rate")
    val hourRate: Double,

    @ColumnInfo(name = "floor_number")
    val floorNumber: String,

    @ColumnInfo(name = "floor_type")
    val floorType: ParkingFloorType?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "lot_identifier")
    val lotIdentifier: String,

    @ColumnInfo(name = "parking_time")
    val parkingTime: DateTime,

    @ColumnInfo(name = "parking_type")
    val parkingType: ParkingType,

    @ColumnInfo(name = "photo")
    val photo: Uri?,

    @ColumnInfo(name = "status")
    val status: ParkingSpotStatus
) : Serializable
