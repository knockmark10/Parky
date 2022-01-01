package com.markoid.parky.settings.presentation.managers

interface DevicePreferences {
    var bluetoothDevice: String
    val darkModeTheme: Int
    var isAutoParkingDetectionEnabled: Boolean
    var isBackgroundLocationEnabled: Boolean
    var isDarkModeEnabled: Boolean
    var isParkingSpotActive: Boolean
    var favoriteParkingType: String
    var hourRate: Double
    var locationAccuracy: Float
    var mapType: Int
    var onBoardingCompleted: Boolean
    val themeResId: Int
}
