package com.markoid.parky.settings.presentation.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DevicePreferencesImpl
@Inject constructor(
    @ApplicationContext context: Context,
    sharedPreferences: SharedPreferences
) : AbstractPreferences(context, sharedPreferences), DevicePreferences {

    override var currentTheme: Int
        get() = getSharedPreference(R.string.dark_mode_key, false).asTheme()
        set(value) = setSharedPreference(R.string.dark_mode_key, value.isDarkMode)

    override var isAutoParkingDetectionEnabled: Boolean
        get() = getSharedPreference(R.string.auto_detection_enabled_key, false)
        set(value) = setSharedPreference(R.string.auto_detection_enabled_key, value)

    override var isBackgroundLocationEnabled: Boolean
        get() = getSharedPreference(R.string.background_location_enabled_key, false)
        set(value) = setSharedPreference(R.string.background_location_enabled_key, value)

    override var isParkingSpotActive: Boolean
        get() = getSharedPreference(R.string.parking_spot_active, false)
        set(value) = setSharedPreference(R.string.parking_spot_active, value)

    override var mapType: Int
        get() = getSharedPreference(R.string.map_type, GoogleMap.MAP_TYPE_HYBRID)
        set(value) = setSharedPreference(R.string.map_type, value)

    private fun Boolean.asTheme(): Int =
        if (this) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
}

val Int.isDarkMode: Boolean
    get() = this == AppCompatDelegate.MODE_NIGHT_YES
