package com.markoid.parky.home.presentation.factories

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.home.presentation.services.CleanUpArchivedSpotsWorker
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import javax.inject.Inject

/**
 * This is the right way to do it. Check the solution at 22 Nov 2021
 *
 * Reference: https://github.com/google/dagger/issues/2601
 */
class CleanUpArchivedSpotWorkerFactory
@Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val parkingRepository: ParkingRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = CleanUpArchivedSpotsWorker(
        appContext,
        workerParameters,
        devicePreferences,
        parkingRepository
    )
}
