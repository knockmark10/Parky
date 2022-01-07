package com.markoid.parky.home.domain.usecases

import android.content.Context
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.presentation.services.BluetoothService
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StopBluetoothServiceUseCase
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val devicePreferences: DevicePreferences
) : UseCase<Unit, Unit>() {

    override suspend fun onExecute(request: Unit) {
        BluetoothService.stopBluetoothService(context)
        devicePreferences.isAutoParkingDetectionEnabled = false
    }
}
