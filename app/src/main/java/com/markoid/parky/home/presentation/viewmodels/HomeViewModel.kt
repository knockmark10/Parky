package com.markoid.parky.home.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.observers.UseCaseObserver
import com.markoid.parky.core.presentation.viewmodels.AbstractViewModel
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.domain.usecases.* // ktlint-disable no-wildcard-imports
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.GetUserLocationUpdatesUseCase
import com.markoid.parky.home.domain.usecases.response.HourRateResponse
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.domain.usecases.GetCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    deleteParkingSpotUseCase: DeleteParkingSpotUseCase,
    finishParkingUseCase: FinishParkingUseCase,
    getActiveParkingSpotUseCase: GetActiveParkingSpotUseCase,
    getCurrentLocationUseCase: GetCurrentLocationUseCase,
    getHourRateDataUseCase: GetHourRateDataUseCase,
    getParkingHistoryUseCase: GetParkingHistoryUseCase,
    private val getUserLocationUpdatesUseCase: GetUserLocationUpdatesUseCase,
    takeCarPictureUseCase: TakeCarPictureUseCase,
    saveParkingSpotUseCase: SaveParkingSpotUseCase,
    dispatcherProvider: CoroutineDispatcherProvider
) : AbstractViewModel() {

    private val activeParkingSpotObserver =
        UseCaseObserver(getActiveParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val currentLocationObserver =
        UseCaseObserver(getCurrentLocationUseCase, dispatcherProvider, viewModelScope)

    private val hourRateObserver =
        UseCaseObserver(getHourRateDataUseCase, dispatcherProvider, viewModelScope)

    private val deleteSpotObserver =
        UseCaseObserver(deleteParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val finishParkingObserver =
        UseCaseObserver(finishParkingUseCase, dispatcherProvider, viewModelScope)

    private val parkingHistoryObserver =
        UseCaseObserver(getParkingHistoryUseCase, dispatcherProvider, viewModelScope)

    private val saveParkingSpotObserver =
        UseCaseObserver(saveParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val takeCarPictureObserver =
        UseCaseObserver(takeCarPictureUseCase, dispatcherProvider, viewModelScope)

    fun getUserLocationUpdates(parkingSpotLocation: LatLng) =
        getUserLocationUpdatesUseCase.subscribeToLocationUpdates(parkingSpotLocation)
            .shareIn(
                viewModelScope,
                started = SharingStarted.Lazily
            )

    fun deleteParkingSpot(parkingSpotId: Long): UseCaseObserver<Unit, Long> =
        deleteSpotObserver.execute(parkingSpotId)

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

    fun saveParkingSpot(request: ParkingSpotRequest): UseCaseObserver<ParkingValidationStatus, ParkingSpotRequest> =
        saveParkingSpotObserver.execute(request)

    fun takeCarPicture() = takeCarPictureObserver.execute(Unit)

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
