package com.markoid.parky.home.presentation.controllers

import android.content.Context
import com.markoid.parky.home.domain.usecases.GetActiveParkingSpotUseCase
import com.markoid.parky.home.presentation.utils.AlarmUtils
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmController
@Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val devicePreferences: DevicePreferences,
    private val getParkingSpotUseCase: GetActiveParkingSpotUseCase
) {

    fun setAlarm(context: Context): Job = coroutineScope.launch(errorHandler) {
        if (devicePreferences.isParkingSpotActive) {
            val parkingSpot = getParkingSpotUseCase.onExecute(Unit)
            parkingSpot.alarmTime?.let { AlarmUtils.setAlarm(context, it) }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
}
