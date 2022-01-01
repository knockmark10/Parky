package com.markoid.parky.settings.presentation.managers

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.GoogleMap
import com.markoid.parky.R
import com.markoid.parky.home.presentation.enums.ParkingType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DevicePreferencesImpl
@Inject constructor(
    @ApplicationContext private val context: Context,
    sharedPreferences: SharedPreferences
) : AbstractPreferences(context, sharedPreferences), DevicePreferences {

    private val res: Resources
        get() = context.resources

    private val packageName: String
        get() = context.packageName

    private val defaultSelectedTheme: Int
        get() {
            val defaultThemeName = getKey(R.string.default_theme)
            val themeResId = getTheme(defaultThemeName)
            return if (themeResId == 0) R.style.Theme_Parky else themeResId
        }

    override var bluetoothDevice: String
        get() = getPreference(R.string.bluetooth_device_key, getKey(R.string.any))
        set(value) = setPreference(R.string.bluetooth_device_key, value)

    override val themeResId: Int
        get() {
            val defaultThemeResId = defaultSelectedTheme
            val themeName = getPreference(
                R.string.current_theme_key,
                res.getResourceEntryName(defaultThemeResId)
            )
            val themeResId = getTheme(themeName)
            return if (themeResId == 0) defaultThemeResId else themeResId
        }

    override val darkModeTheme: Int
        get() = (getPreference(R.string.dark_mode_key, false)).asTheme()

    override var isDarkModeEnabled: Boolean
        get() = getPreference(R.string.dark_mode_key, false)
        set(value) = setPreference(R.string.dark_mode_key, value)

    override var favoriteParkingType: String
        get() = getPreference(
            R.string.favorite_parking_key,
            ParkingType.StreetParking.name
        )
        set(value) = setPreference(R.string.favorite_parking_key, value)

    override var hourRate: Double
        get() = getPreference(R.string.hour_rate_key, 10.0)
        set(value) = setPreference(R.string.hour_rate_key, value)

    override var locationAccuracy: Float
        get() = getPreference(R.string.location_accuracy_key, 20f)
        set(value) = setPreference(R.string.location_accuracy_key, value)

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

    override var onBoardingCompleted: Boolean
        get() = getPreference(R.string.onboarding_completed_key, false)
        set(value) = setPreference(R.string.onboarding_completed_key, value)

    private fun getTheme(themeName: String): Int =
        res.getIdentifier(themeName, "style", packageName)

    private fun Boolean.asTheme(): Int =
        if (this) AppCompatDelegate.MODE_NIGHT_YES
        else AppCompatDelegate.MODE_NIGHT_NO
}
