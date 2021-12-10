package com.markoid.parky.position.data.mappers

import android.location.Address
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.data.entities.bigdatacloud.BigDataCloudResponse
import com.markoid.parky.position.data.entities.positionstack.PositionStackData
import com.markoid.parky.position.data.enums.CountryState

fun BigDataCloudResponse.toPositionEntity(): PositionEntity {
    val state = CountryState.getStateFromCode(principalSubdivisionCode)
        ?: CountryState.getStateFromCode(principalSubdivisionCode)
        ?: CountryState.MEX_DF
    return PositionEntity(
        city = cityName,
        country = state.country,
        latitude = latitude,
        longitude = longitude,
        state = state,
        streetAddress = "N/A"
    )
}

fun PositionStackData.toPositionEntity(): PositionEntity {
    val state = CountryState.getStateFromCode(regionCode)
        ?: CountryState.getStateFromName(region)
        ?: CountryState.MEX_DF
    return PositionEntity(
        city = if (locality.isNotEmpty()) locality else region,
        country = state.country,
        latitude = latitude,
        longitude = longitude,
        state = state,
        streetAddress = fullAddress
    )
}

fun Address.toPositionEntity(): PositionEntity {
    val state = CountryState.getStateFromName(adminArea) ?: CountryState.MEX_DF
    return PositionEntity(
        city = locality.orEmpty(),
        country = state.country,
        latitude = latitude,
        longitude = longitude,
        state = state,
        streetAddress = getAddressLine(0).orEmpty()
    )
}
