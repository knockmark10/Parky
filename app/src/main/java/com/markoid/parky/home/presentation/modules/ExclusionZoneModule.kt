package com.markoid.parky.home.presentation.modules

import com.markoid.parky.core.data.database.ParkyDatabase
import com.markoid.parky.home.data.dao.ExclusionZoneDao
import com.markoid.parky.home.data.datasources.ExclusionZoneDataSource
import com.markoid.parky.home.data.datasources.ExclusionZoneDataSourceImpl
import com.markoid.parky.home.domain.repositories.ExclusionZoneRepository
import com.markoid.parky.home.domain.repositories.ExclusionZoneRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ExclusionZoneModule {

    @Provides
    fun providesExclusionZoneDao(
        database: ParkyDatabase
    ): ExclusionZoneDao = database.exclusionZoneDao()

    @Provides
    fun providesExclusionZoneDataSource(
        exclusionZoneDataSource: ExclusionZoneDataSourceImpl
    ): ExclusionZoneDataSource = exclusionZoneDataSource

    @Provides
    fun providesExclusionZoneRepository(
        repositoryImpl: ExclusionZoneRepositoryImpl
    ): ExclusionZoneRepository = repositoryImpl
}
