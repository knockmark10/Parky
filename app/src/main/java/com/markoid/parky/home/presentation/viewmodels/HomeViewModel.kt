package com.markoid.parky.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.observers.UseCaseObserver
import com.markoid.parky.home.domain.usecases.SaveParkingSpotUseCase
import com.markoid.parky.home.domain.usecases.TakeCarPictureUseCase
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.domain.usecases.GetCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    getCurrentLocationUseCase: GetCurrentLocationUseCase,
    takeCarPictureUseCase: TakeCarPictureUseCase,
    saveParkingSpotUseCase: SaveParkingSpotUseCase,
    dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val currentLocationObserver =
        UseCaseObserver(getCurrentLocationUseCase, dispatcherProvider, viewModelScope)

    private val saveParkingSpotObserver =
        UseCaseObserver(saveParkingSpotUseCase, dispatcherProvider, viewModelScope)

    private val takeCarPictureObserver =
        UseCaseObserver(takeCarPictureUseCase, dispatcherProvider, viewModelScope)

    fun getCurrentLocation(): UseCaseObserver<PositionEntity, Unit> =
        currentLocationObserver.execute(Unit)

    fun saveParkingSpot(request: ParkingSpotRequest): UseCaseObserver<ParkingValidationStatus, ParkingSpotRequest> =
        saveParkingSpotObserver.execute(request)

    fun takeCarPicture() = takeCarPictureObserver.execute(Unit)

    override fun onCleared() {
        super.onCleared()
        saveParkingSpotObserver.dispose()
        currentLocationObserver.dispose()
    }
}
