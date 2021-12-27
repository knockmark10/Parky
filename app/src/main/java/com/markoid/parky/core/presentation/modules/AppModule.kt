package com.markoid.parky.core.presentation.modules

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.markoid.parky.core.data.database.ParkyDatabase
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.dispatchers.DefaultCoroutineDispatcherProvider
import com.markoid.parky.core.presentation.managers.NotificationManager
import com.markoid.parky.home.presentation.controllers.AlarmController
import com.markoid.parky.home.presentation.receivers.helpers.AlarmControllerInjector
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import com.markoid.parky.settings.presentation.managers.DevicePreferencesImpl
import com.markoid.permissions.managers.abstractions.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun providesCoroutineDispatcher(): CoroutineDispatcherProvider =
        DefaultCoroutineDispatcherProvider()

    @Provides
    @Singleton
    fun providesCoroutineScope(dispatcherProvider: CoroutineDispatcherProvider): CoroutineScope =
        CoroutineScope(dispatcherProvider.io)

    @Provides
    @Singleton
    fun providesPermissionManager(): PermissionManager = PermissionManager.getInstance()

    @Provides
    @Singleton
    fun providesSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun providesDevicePreferences(devicePreferences: DevicePreferencesImpl): DevicePreferences =
        devicePreferences

    @Provides
    @Singleton
    fun providesAlarmController(
        @ApplicationContext context: Context,
        database: ParkyDatabase
    ): AlarmController = AlarmControllerInjector(context, database).getAlarmController()

    @Provides
    @Singleton
    fun providesNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = NotificationManager(context)
}
