package com.markoid.parky.home.presentation.controllers

import android.content.Context
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.home.presentation.utils.AlarmUtils
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmController @Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val coroutineScope: CoroutineScope,
    private val parkingRepository: ParkingRepository
) {

    fun setAlarm(context: Context): Job = coroutineScope.launch(errorHandler) {
        if (devicePreferences.isParkingSpotActive) {
            val parkingSpot = parkingRepository.getActiveParkingSpot()
            parkingSpot.alarmTime?.let { AlarmUtils.setAlarm(context, it) }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
}
