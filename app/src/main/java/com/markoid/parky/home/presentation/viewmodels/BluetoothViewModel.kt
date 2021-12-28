package com.markoid.parky.home.presentation.viewmodels

import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.observers.UseCaseFlowObserver
import com.markoid.parky.home.domain.usecases.SaveParkingSpotAutoUseCase
import com.markoid.parky.home.domain.usecases.response.AutoParkingSpotStatus
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class BluetoothViewModel
@Inject constructor(
    coroutineScope: CoroutineScope,
    dispatcherProvider: CoroutineDispatcherProvider,
    saveAutoParkingSpotUseCase: SaveParkingSpotAutoUseCase
) {

    private val saveParkingSpotObserver =
        UseCaseFlowObserver(saveAutoParkingSpotUseCase, dispatcherProvider, coroutineScope)

    fun saveAutoParkingSpot(
        deviceName: String
    ): UseCaseFlowObserver<AutoParkingSpotStatus, String> = saveParkingSpotObserver
        .execute(deviceName)
}
