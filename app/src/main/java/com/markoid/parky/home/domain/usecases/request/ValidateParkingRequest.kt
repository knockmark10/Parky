package com.markoid.parky.home.domain.usecases.request

data class ValidateParkingRequest(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val parkingTime: String,
    val parkingType: String,
    val floorType: String,
    val floorNumber: String,
    val color: String,
    val lotIdentifier: String,
    val fare: Double
)
