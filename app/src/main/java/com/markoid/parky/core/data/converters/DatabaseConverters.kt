package com.markoid.parky.core.data.converters

import android.net.Uri
import androidx.room.TypeConverter
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import org.joda.time.DateTime

class DatabaseConverters {

    @TypeConverter
    fun floorTypeToString(type: ParkingFloorType?): String? = type?.name

    @TypeConverter
    fun stringFromFloorType(value: String?): ParkingFloorType? =
        value?.let { ParkingFloorType.valueOf(value) }

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
    fun dateTimeToLong(time: DateTime?): Long? = time?.millis

    @TypeConverter
    fun longFromDateTime(millis: Long?): DateTime? = millis?.let { DateTime(millis) }

    @TypeConverter
    fun uriToString(uri: Uri?): String? = uri?.toString()

    @TypeConverter
    fun uriFromString(uriString: String?): Uri? = uriString?.let { Uri.parse(it) }

    @TypeConverter
    fun colorTypeToString(color: ParkingColor?): String? = color?.name

    @TypeConverter
    fun colorTypeFromString(colorName: String?): ParkingColor? =
        colorName?.let { ParkingColor.valueOf(it) }
}
