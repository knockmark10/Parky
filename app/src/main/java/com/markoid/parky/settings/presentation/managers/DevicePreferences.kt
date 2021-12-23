package com.markoid.parky.settings.presentation.managers

interface DevicePreferences {
    var currentTheme: Int
    var isAutoParkingDetectionEnabled: Boolean
    var isBackgroundLocationEnabled: Boolean
    var isParkingSpotActive: Boolean
    var mapType: Int
}
