package com.markoid.parky.core.presentation.modules

import android.content.Context
import android.content.res.Resources
import com.markoid.parky.core.presentation.dispatchers.CoroutineDispatcherProvider
import com.markoid.parky.core.presentation.dispatchers.DefaultCoroutineDispatcherProvider
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
    fun providesPermissionManager(): PermissionManager = PermissionManager.getInstance()
}
