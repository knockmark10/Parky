package com.markoid.parky.settings.presentation.managers

interface DevicePreferences {
    var bluetoothDevice: String
    val themeResId: Int
    val darkModeTheme: Int
    var isDarkModeEnabled: Boolean
    var favoriteParkingType: String
    var hourRate: Double
    var isAutoParkingDetectionEnabled: Boolean
    var isBackgroundLocationEnabled: Boolean
    var isParkingSpotActive: Boolean
    var mapType: Int
    var onBoardingCompleted: Boolean
}
