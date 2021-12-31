package com.markoid.parky.splash.presentation.activities

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.markoid.parky.home.presentation.activities.HomeActivity
import com.markoid.parky.home.presentation.receivers.BootCompleteReceiver
import com.markoid.parky.onboarding.activities.OnBoardingActivity
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var devicePreferences: DevicePreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(devicePreferences.darkModeTheme)

        applicationContext.packageManager.setComponentEnabledSetting(
            ComponentName(applicationContext, BootCompleteReceiver::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        routeIntent()
    }

    private fun routeIntent() {
        val intent = when {
            devicePreferences.onBoardingCompleted -> Intent(this, HomeActivity::class.java)
            else -> Intent(this, OnBoardingActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
