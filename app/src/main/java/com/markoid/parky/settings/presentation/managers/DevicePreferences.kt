package com.markoid.parky.settings.presentation.managers

interface DevicePreferences {
    var bluetoothDevice: String
    var currentTheme: Int
    var favoriteParkingType: String
    var hourRate: Double
    var isAutoParkingDetectionEnabled: Boolean
    var isBackgroundLocationEnabled: Boolean
    var isParkingSpotActive: Boolean
    var mapType: Int
}
