package com.markoid.parky.home.data.entities

enum class ParkingSpotStatus {
    Active,
    Archived
}

val ParkingSpotStatus.isActive: Boolean
    get() = this == ParkingSpotStatus.Active

val ParkingSpotStatus.isArchived: Boolean
    get() = this == ParkingSpotStatus.Archived
