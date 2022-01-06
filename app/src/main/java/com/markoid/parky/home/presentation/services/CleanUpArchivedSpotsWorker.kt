package com.markoid.parky.home.presentation.services

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.markoid.parky.core.presentation.extensions.days
import com.markoid.parky.core.presentation.extensions.untilToday
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.joda.time.Duration
import java.util.concurrent.TimeUnit

@HiltWorker
class CleanUpArchivedSpotsWorker
@AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val devicePreferences: DevicePreferences,
    private val repository: ParkingRepository
) : CoroutineWorker(context, params) {

    private val durationDaysToBeRemoved: Duration
        get() = devicePreferences.cleanUpAfterDays.days

    override suspend fun doWork(): Result = try {
        // Retrieve archived parking spots from local storage
        val archivedSpots: List<ParkingSpotEntity> = repository
            .getAllParkingSpots()
            .filter { it.status == ParkingSpotStatus.Archived }
        // Loop through all of them and delete the ones over 30 days
        archivedSpots.forEach {
            val duration = it.parkingTime.untilToday()
            if (duration.isLongerThan(durationDaysToBeRemoved)) repository.deleteParkingSpot(it.id)
        }
        // Return success
        Result.success()
    } catch (exception: Throwable) {
        exception.printStackTrace()
        Result.retry()
    }

    companion object {
        private const val CLEAN_UP_WORKER_NAME = "Clean_Up_Worker_Name"
        fun startWorker(context: Context) {
            // Set the constraints for the worker
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()

            // Set the work to be performed once every 24 hours
            val work = PeriodicWorkRequestBuilder<CleanUpArchivedSpotsWorker>(24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

            // Schedule the work
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    CLEAN_UP_WORKER_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    work
                )
        }
    }
}
