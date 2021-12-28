package com.markoid.parky.settings.presentation.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import com.markoid.parky.home.presentation.enums.ParkingType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DevicePreferencesImpl
@Inject constructor(
    @ApplicationContext context: Context,
    sharedPreferences: SharedPreferences
) : AbstractPreferences(context, sharedPreferences), DevicePreferences {

    override var bluetoothDevice: String
        get() = getPreference(R.string.bluetooth_device_key, getKey(R.string.any))
        set(value) = setPreference(R.string.bluetooth_device_key, value)

    override var currentTheme: Int
        get() = getPreference(R.string.dark_mode_key, false).asTheme()
        set(value) = setPreference(R.string.dark_mode_key, value.isDarkMode)

    override var favoriteParkingType: String
        get() = getPreference(
            R.string.favorite_parking_key,
            getKey(ParkingType.StreetParking.typeId)
        )
        set(value) = setPreference(R.string.favorite_parking_key, value)

    override var hourRate: Double
        get() = getPreference(R.string.hour_rate_key, 10.0)
        set(value) = setPreference(R.string.hour_rate_key, value)

    override var isAutoParkingDetectionEnabled: Boolean
        get() = getPreference(R.string.auto_detection_enabled_key, false)
        set(value) = setPreference(R.string.auto_detection_enabled_key, value)

    override var isBackgroundLocationEnabled: Boolean
        get() = getPreference(R.string.background_location_enabled_key, false)
        set(value) = setPreference(R.string.background_location_enabled_key, value)

    override var isParkingSpotActive: Boolean
        get() = getPreference(R.string.parking_spot_active, false)
        set(value) = setPreference(R.string.parking_spot_active, value)

    override var mapType: Int
        get() = getPreference(R.string.map_type, GoogleMap.MAP_TYPE_HYBRID)
        set(value) = setPreference(R.string.map_type, value)

    private fun Boolean.asTheme(): Int =
        if (this) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
}

val Int.isDarkMode: Boolean
    get() = this == AppCompatDelegate.MODE_NIGHT_YES
