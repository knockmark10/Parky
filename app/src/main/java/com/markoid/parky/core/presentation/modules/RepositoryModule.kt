package com.markoid.parky.core.presentation.modules

import android.content.Context
import androidx.room.Room
import com.markoid.parky.core.data.database.ParkyDatabase
import com.markoid.parky.core.presentation.utils.DatabaseConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): ParkyDatabase =
        Room.databaseBuilder(
            context,
            ParkyDatabase::class.java,
            DatabaseConstants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
}
