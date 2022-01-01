package com.markoid.parky.position.data.datasources

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.data.handlers.ResponseHandler
import com.markoid.parky.core.presentation.extensions.mapTo
import com.markoid.parky.position.data.entities.bigdatacloud.BigDataCloudResponse
import com.markoid.parky.position.data.entities.positionstack.PositionStackResponse
import com.markoid.parky.position.data.services.BigDataCloudService
import com.markoid.parky.position.data.services.PositionStackService
import com.markoid.parky.position.presentation.managers.PositionManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackingDataSourceImpl
@Inject constructor(
    private val cloudService: BigDataCloudService,
    private val positionManager: PositionManager,
    private val stackService: PositionStackService,
    private val responseHandler: ResponseHandler
) : TrackingDataSource {

    override suspend fun getCurrentLocation(): Location =
        this.positionManager.requestSingleLocation()

    override fun getRealTimeLocation(): Flow<Location> =
        positionManager.observeLocationUpdates()

    override suspend fun translateCoordinatesWithAndroidApi(location: LatLng): List<Address> =
        this.positionManager.getAddressFromLocation(location) ?: emptyList()

    override suspend fun translateCoordinatesWithBigDataCloud(location: LatLng): BigDataCloudResponse =
        this.cloudService.reverseGeocoding(location.latitude, location.longitude)
            .mapTo { responseHandler.handleResponse(it) }

    override suspend fun translateCoordinatesWithPositionStack(latLng: String): PositionStackResponse =
        this.stackService.reverseGeocoding(latLng)
            .mapTo { responseHandler.handleResponse(it) }

    override suspend fun translateLocationNameWithPositionStack(latLng: String): PositionStackResponse =
        this.stackService.forwardGeocoding(latLng)
            .mapTo { responseHandler.handleResponse(it) }
}
