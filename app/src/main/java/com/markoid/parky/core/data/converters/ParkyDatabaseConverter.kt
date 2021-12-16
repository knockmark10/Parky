package com.markoid.parky.core.data.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import org.joda.time.DateTime

class ParkyDatabaseConverter {

    @TypeConverter
    fun floorTypeToString(type: ParkingFloorType): String = type.name

    @TypeConverter
    fun stringFromFloorType(value: String): ParkingFloorType = ParkingFloorType.valueOf(value)

    @TypeConverter
    fun parkingTypeToString(type: ParkingType): String = type.name

    @TypeConverter
    fun stringFromParkingType(value: String): ParkingType = ParkingType.valueOf(value)

    @TypeConverter
    fun parkingSpotStatusFromString(status: ParkingSpotStatus): String = status.name

    @TypeConverter
    fun stringFromParkingSpotStatus(value: String): ParkingSpotStatus =
        ParkingSpotStatus.valueOf(value)

    @TypeConverter
    fun dateTimeToLong(time: DateTime): Long = time.millis

    @TypeConverter
    fun longFromDateTime(millis: Long): DateTime = DateTime(millis)

    @TypeConverter
    fun uriToString(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun uriFromString(uriString: String?): Uri? = uriString?.let { Uri.parse(it) }
}
