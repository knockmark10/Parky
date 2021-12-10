package com.markoid.parky

import android.app.Application
import com.markoid.parky.core.presentation.providers.ActivityProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ParkyApplication : Application() {

    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()
        activityProvider.init(this)
    }
}
