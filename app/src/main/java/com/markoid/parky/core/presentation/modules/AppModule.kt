package com.markoid.parky.core.presentation.modules

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.dispatchers.DefaultCoroutineDispatcherProvider
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import com.markoid.parky.settings.presentation.managers.DevicePreferencesImpl
import com.markoid.permissions.managers.abstractions.PermissionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun providesPermissionManager(): PermissionManager = PermissionManager.getInstance()

    @Provides
    @Singleton
    fun providesSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun providesDevicePreferences(devicePreferences: DevicePreferencesImpl): DevicePreferences =
        devicePreferences
}
