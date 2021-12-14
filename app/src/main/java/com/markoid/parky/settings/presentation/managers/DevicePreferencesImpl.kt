package com.markoid.parky.settings.presentation.managers

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.markoid.parky.R
import javax.inject.Inject

class DevicePreferencesImpl
@Inject constructor(
    private val resources: Resources,
    private val sharedPreferences: SharedPreferences
) : DevicePreferences {

    override fun isAutoParkingDetectionEnabled(): Boolean =
        sharedPreferences.getBoolean(
            resources.getString(R.string.auto_detection_enabled_key),
            false
        )

    override fun isBackgroundLocationEnabled(): Boolean =
        sharedPreferences.getBoolean(
            resources.getString(R.string.background_location_enabled_key),
            false
        )

    override fun getCurrentTheme(): Int =
        if (sharedPreferences.getBoolean(resources.getString(R.string.dark_mode_key), false)) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
}
