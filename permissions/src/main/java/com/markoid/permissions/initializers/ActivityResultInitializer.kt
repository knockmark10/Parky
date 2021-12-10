package com.markoid.permissions.initializers

import android.app.Application
import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import com.markoid.permissions.managers.abstractions.ActivityResultManager

class ActivityResultInitializer : Initializer<ActivityResultManager> {
    override fun create(context: Context): ActivityResultManager {

        val appInitializer = AppInitializer.getInstance(context)
        check(appInitializer.isEagerlyInitialized(javaClass)) {
            """ActivityResultInitializer cannot be initialized lazily. 
                Please ensure that you have: 
                <meta-data
                    android:name="com.markoid.permissions.initializers.ActivityResultInitializer"
                    android:value="androidx.startup" />
                under InitializationProvider in your AndroidManifest.xml"""
        }
        val application = context as Application
        ActivityResultManager.init(application)
        return ActivityResultManager.getInstance()
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}
