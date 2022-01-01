package com.markoid.parky

import android.app.Application
import com.facebook.stetho.Stetho
import com.markoid.parky.core.presentation.providers.ActivityProvider
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class ParkyApplication : Application() {

    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()
        activityProvider.init(this)
        Stetho.initializeWithDefaults(this)
        Lingver.init(this, Locale.getDefault())
    }
}
