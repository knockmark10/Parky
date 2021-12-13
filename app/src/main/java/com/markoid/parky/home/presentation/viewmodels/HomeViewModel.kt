package com.markoid.parky.home.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.observers.UseCaseObserver
import com.markoid.parky.home.domain.usecases.ValidateNewParkingUseCase
import com.markoid.parky.home.domain.usecases.request.ValidateParkingRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.domain.usecases.GetCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    getCurrentLocationUseCase: GetCurrentLocationUseCase,
    validateNewParkingUseCase: ValidateNewParkingUseCase,
    dispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val currentLocationObserver =
        UseCaseObserver(getCurrentLocationUseCase, dispatcherProvider, viewModelScope)

    private val validateParkingObserver =
        UseCaseObserver(validateNewParkingUseCase, dispatcherProvider, viewModelScope)

    fun getCurrentLocation(): UseCaseObserver<PositionEntity, Unit> =
        currentLocationObserver.execute(Unit)

    fun validateNewParking(request: ValidateParkingRequest): UseCaseObserver<ParkingValidationStatus, ValidateParkingRequest> =
        validateParkingObserver.execute(request)

    override fun onCleared() {
        super.onCleared()
        validateParkingObserver.dispose()
        currentLocationObserver.dispose()
    }
}
