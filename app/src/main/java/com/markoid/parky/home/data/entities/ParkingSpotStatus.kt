package com.markoid.parky.home.data.entities

enum class ParkingSpotStatus {
    Active,
    Archived
}

val ParkingSpotStatus.isActive: Boolean
    get() = this == ParkingSpotStatus.Active
