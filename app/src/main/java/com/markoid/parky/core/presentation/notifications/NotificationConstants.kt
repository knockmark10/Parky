package com.markoid.parky.core.presentation.notifications

object NotificationConstants {

    /**
     * CHANNEL PARAMETERS
     */
    const val REMINDER_ALARM_CHANNEL_ID = "ReminderAlarmId"
    const val AUTO_PARKING_CHANNEL_ID = "AutoParkingSpotId"
    const val BLUETOOTH_CHANNEL_ID = "BluetoothId"

    /**
     * INTENT ARGUMENT KEYS
     */
    const val PARKING_SPOT_REQUEST_ARG = "parking.spot.request.arg"

    /**
     * VIBRATION PATTERNS
     */
    val urgentVibrationPattern =
        longArrayOf(0, 1000, 100, 1000, 100, 1000, 100, 1000, 100, 1000, 100, 1000, 100, 1000, 100)

    val regularVibrationPattern = longArrayOf(0, 1000, 1000, 1000, 1000, 1000, 100)
}
