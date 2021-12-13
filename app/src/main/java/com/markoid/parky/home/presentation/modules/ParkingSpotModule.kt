package com.markoid.parky.home.presentation.modules

import com.markoid.parky.core.data.database.ParkyDatabase
import com.markoid.parky.home.data.dao.ParkingSpotDao
import com.markoid.parky.home.data.datasources.ParkingDataSource
import com.markoid.parky.home.data.datasources.ParkingDataSourceImpl
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.home.domain.repositories.ParkingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ParkingSpotModule {

    @Provides
    fun providesParkingSpotDao(database: ParkyDatabase): ParkingSpotDao =
        database.parkingSpotDao()

    @Provides
    fun providesParkingDataSource(parkingDataSourceImpl: ParkingDataSourceImpl): ParkingDataSource =
        parkingDataSourceImpl

    @Provides
    fun providesParkingRepository(repository: ParkingRepositoryImpl): ParkingRepository =
        repository
}