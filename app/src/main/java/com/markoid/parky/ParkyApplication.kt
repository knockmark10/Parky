package com.markoid.parky

import android.app.Application
import androidx.work.Configuration
import com.facebook.stetho.Stetho
import com.markoid.parky.core.presentation.providers.ActivityProvider
import com.markoid.parky.home.presentation.factories.CleanUpArchivedSpotWorkerFactory
import com.markoid.parky.home.presentation.services.CleanUpArchivedSpotsWorker
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class ParkyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var activityProvider: ActivityProvider

    @Inject
    lateinit var cleanUpArchivedSpotWorkerFactory: CleanUpArchivedSpotWorkerFactory

    override fun onCreate() {
        super.onCreate()
        activityProvider.init(this)
        Stetho.initializeWithDefaults(this)
        Lingver.init(this, Locale.getDefault())
        CleanUpArchivedSpotsWorker.startWorker(this)
    }

    override fun getWorkManagerConfiguration(): Configuration = Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .setWorkerFactory(cleanUpArchivedSpotWorkerFactory)
        .build()
}
