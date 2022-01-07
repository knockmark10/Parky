package com.markoid.parky.core.presentation.notifications

object NotificationConstants {

    /**
     * CHANNEL PARAMETERS
     */
    const val AUTO_ARCHIVED_PARKING_CHANNEL_ID = "ArchiveParkingSpotId"
    const val AUTO_PARKING_CHANNEL_ID = "AutoParkingSpotId"
    const val BLUETOOTH_CHANNEL_ID = "BluetoothId"
    const val REMINDER_ALARM_CHANNEL_ID = "ReminderAlarmId"

    /**
     * INTENT ARGUMENT KEYS
     */
    const val PARKING_SPOT_REQUEST_ARG = "parking.spot.request.arg"

    /**
     * VIBRATION PATTERNS
     */
    val urgentVibrationPattern =
        longArrayOf(0, 1000, 100, 1000, 100, 1000, 100, 1000, 100, 1000, 100, 1000, 100, 1000, 100)

    val regularVibrationPattern = longArrayOf(0, 400, 400, 400, 400, 400, 100)
}
