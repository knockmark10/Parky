package com.markoid.parky.home.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.home.presentation.enums.ParkingColor
import java.io.Serializable

@Entity(tableName = "exclusion_zones")
data class ExclusionZoneEntity(
    @ColumnInfo(name = "color")
    val color: ParkingColor,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "radius")
    val radius: Double
) : Serializable {

    val location: LatLng
        get() = LatLng(latitude, longitude)
}
