package com.markoid.parky.home.presentation.receivers.helpers

import android.content.Context
import androidx.preference.PreferenceManager
import com.markoid.parky.core.data.database.ParkyDatabase
import com.markoid.parky.home.data.dao.ParkingSpotDao
import com.markoid.parky.home.data.datasources.ParkingDataSource
import com.markoid.parky.home.data.datasources.ParkingDataSourceImpl
import com.markoid.parky.home.data.mappers.ParkingSpotMapper
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.home.domain.repositories.ParkingRepositoryImpl
import com.markoid.parky.home.presentation.controllers.AlarmController
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import com.markoid.parky.settings.presentation.managers.DevicePreferencesImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * This class is necessary to inject it into BroadcastReceiver
 */
class AlarmControllerInjector(
    private val appContext: Context,
    private val database: ParkyDatabase
) {

    fun getAlarmController(): AlarmController = AlarmController(
        providesDevicePreferences(),
        providesCoroutineScope(),
        providesParkingRepository()
    )

    private fun providesDevicePreferences(): DevicePreferences = DevicePreferencesImpl(
        appContext,
        PreferenceManager.getDefaultSharedPreferences(appContext)
    )

    private fun providesCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    private fun providesParkingRepository(): ParkingRepository = ParkingRepositoryImpl(
        providesParkingDataSource(),
        providesParkingMapper()
    )

    private fun providesParkingDataSource(): ParkingDataSource = ParkingDataSourceImpl(
        providesParkingSpotDao()
    )

    private fun providesParkingMapper(): ParkingSpotMapper = ParkingSpotMapper(
        appContext.resources
    )

    private fun providesParkingSpotDao(): ParkingSpotDao = database.parkingSpotDao()
}
