package com.markoid.parky.settings.presentation.managers

interface DevicePreferences {
    fun isAutoParkingDetectionEnabled(): Boolean
    fun isBackgroundLocationEnabled(): Boolean
    fun getCurrentTheme(): Int
}
