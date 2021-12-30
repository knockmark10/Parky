package com.markoid.parky.home.presentation.viewmodels

import android.location.Location
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.observers.UseCaseObserver
import com.markoid.parky.core.presentation.viewmodels.AbstractViewModel
import com.markoid.parky.home.data.entities.ExclusionZoneEntity
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.UpdateExclusionZoneUseCase
import com.markoid.parky.home.domain.usecases.DeleteExclusionZoneUseCase
import com.markoid.parky.home.domain.usecases.DeleteParkingSpotUseCase
import com.markoid.parky.home.domain.usecases.FinishParkingUseCase
import com.markoid.parky.home.domain.usecases.GetActiveParkingSpotUseCase
import com.markoid.parky.home.domain.usecases.GetExclusionZonesUseCase
import com.markoid.parky.home.domain.usecases.GetHourRateDataUseCase
import com.markoid.parky.home.domain.usecases.GetLocationUpdatesWithParkingSpotDistanceUseCase
import com.markoid.parky.home.domain.usecases.GetParkingHistoryUseCase
import com.markoid.parky.home.domain.usecases.GetRealTimeLocationUseCase
import com.markoid.parky.home.domain.usecases.SaveParkingSpotUseCase
import com.markoid.parky.home.domain.usecases.TakeCarPictureUseCase
import com.markoid.parky.home.domain.usecases.UpdateParkingSpotUseCase
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.HourRateResponse
import com.markoid.parky.home.domain.usecases.response.LocationUpdatesResponse
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.domain.usecases.GetCurrentLocationUseCase
import com.markoid.parky.settings.domain.requests.ExclusionZoneRequest
import com.markoid.parky.settings.domain.responses.ExclusionZoneValidationStatus
import com.markoid.parky.settings.domain.usecases.SaveExclusionZoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    deleteParkingSpotUseCase: DeleteParkingSpotUseCase,
    deleteExclusionZoneUseCase: DeleteExclusionZoneUseCase,
    dispatcherProvider: CoroutineDispatcherProvider,
    finishParkingUseCase: FinishParkingUseCase,
    getActiveParkingSpotUseCase: GetActiveParkingSpotUseCase,
    getCurrentLocationUseCase: GetCurrentLocationUseCase,
    getExclusionZonesUseCase: GetExclusionZonesUseCase,
    getHourRateDataUseCase: GetHourRateDataUseCase,
    getParkingHistoryUseCase: GetParkingHistoryUseCase,
    private val getLocationUpdatesWithSpotUseCase: GetLocationUpdatesWithParkingSpotDistanceUseCase,
    private val getRealTimeLocationUseCase: GetRealTimeLocationUseCase,
    takeCarPictureUseCase: TakeCarPictureUseCase,
    saveParkingSpotUseCase: SaveParkingSpotUseCase,
    saveExclusionZoneUseCase: SaveExclusionZoneUseCase,
    updateExclusionZoneUseCase: UpdateExclusionZoneUseCase,
    updateParkingSpotUseCase: UpdateParkingSpotUseCase
) : AbstractViewModel() {

    private val activeParkingSpotObserver =
        UseCaseObserver(getActiveParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val currentLocationObserver =
        UseCaseObserver(getCurrentLocationUseCase, dispatcherProvider, viewModelScope)

    private val hourRateObserver =
        UseCaseObserver(getHourRateDataUseCase, dispatcherProvider, viewModelScope)

    private val deleteSpotObserver =
        UseCaseObserver(deleteParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val deleteExclusionZoneObserver =
        UseCaseObserver(deleteExclusionZoneUseCase, dispatcherProvider, viewModelScope)

    private val finishParkingObserver =
        UseCaseObserver(finishParkingUseCase, dispatcherProvider, viewModelScope)

    private val getExclusionZonesObserver =
        UseCaseObserver(getExclusionZonesUseCase, dispatcherProvider, viewModelScope)

    private val parkingHistoryObserver =
        UseCaseObserver(getParkingHistoryUseCase, dispatcherProvider, viewModelScope)

    private val saveParkingSpotObserver =
        UseCaseObserver(saveParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val saveExclusionZoneObserver =
        UseCaseObserver(saveExclusionZoneUseCase, dispatcherProvider, viewModelScope)

    private val takeCarPictureObserver =
        UseCaseObserver(takeCarPictureUseCase, dispatcherProvider, viewModelScope)

    private val updateSpotObserver =
        UseCaseObserver(updateParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val updateExclusionZoneObserver =
        UseCaseObserver(updateExclusionZoneUseCase, dispatcherProvider, viewModelScope)

    fun getLocationUpdatesWithSpotDistance(
        parkingSpotLocation: LatLng
    ): SharedFlow<LocationUpdatesResponse> = getLocationUpdatesWithSpotUseCase
        .subscribeToLocationUpdates(parkingSpotLocation)
        .shareIn(
            viewModelScope,
            started = SharingStarted.Lazily
        )

    fun getRealTimeLocation(): SharedFlow<Location> = getRealTimeLocationUseCase
        .getLocationUpdates()
        .shareIn(
            viewModelScope,
            started = SharingStarted.Lazily
        )

    fun deleteParkingSpot(parkingSpotId: Long): UseCaseObserver<Unit, Long> =
        deleteSpotObserver.execute(parkingSpotId)

    fun deleteExclusionZone(id: Long): UseCaseObserver<Unit, Long> =
        deleteExclusionZoneObserver.execute(id)

    fun finishParking(parkingSpotId: Long) =
        finishParkingObserver.execute(parkingSpotId)

    fun getActiveParkingSpot(): UseCaseObserver<ParkingSpotEntity, Unit> =
        activeParkingSpotObserver.execute(Unit)

    fun getCurrentLocation(): UseCaseObserver<PositionEntity, Unit> =
        currentLocationObserver.execute(Unit)

    fun getHourRateData(): UseCaseObserver<HourRateResponse, Unit> =
        hourRateObserver.execute(Unit)

    fun getParkingHistory(): UseCaseObserver<List<ParkingSpotEntity>, Unit> =
        parkingHistoryObserver.execute(Unit)

    fun getExclusionZones(): UseCaseObserver<List<ExclusionZoneEntity>, Unit> =
        getExclusionZonesObserver.execute(Unit)

    fun saveParkingSpot(request: ParkingSpotRequest): UseCaseObserver<ParkingValidationStatus, ParkingSpotRequest> =
        saveParkingSpotObserver.execute(request)

    fun saveExclusionZone(zone: ExclusionZoneRequest): UseCaseObserver<ExclusionZoneValidationStatus, ExclusionZoneRequest> =
        saveExclusionZoneObserver.execute(zone)

    fun takeCarPicture() = takeCarPictureObserver.execute(Unit)

    fun updateParkingSpot(request: ParkingSpotRequest): UseCaseObserver<ParkingValidationStatus, ParkingSpotRequest> =
        updateSpotObserver.execute(request)

    fun updateExclusionZone(
        request: ExclusionZoneRequest
    ): UseCaseObserver<ExclusionZoneValidationStatus, ExclusionZoneRequest> =
        updateExclusionZoneObserver.execute(request)

    override fun onCleared() {
        super.onCleared()
        activeParkingSpotObserver.dispose()
        currentLocationObserver.dispose()
        deleteSpotObserver.dispose()
        parkingHistoryObserver.dispose()
        saveParkingSpotObserver.dispose()
        takeCarPictureObserver.dispose()
    }
}
