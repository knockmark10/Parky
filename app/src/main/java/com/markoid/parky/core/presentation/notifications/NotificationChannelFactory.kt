package com.markoid.parky.core.presentation.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.markoid.parky.core.presentation.notifications.AppNotificationType.AutoParkingSpotMissingData
import com.markoid.parky.core.presentation.notifications.AppNotificationType.AutoParkingSpotRequiresUserInteraction
import com.markoid.parky.core.presentation.notifications.AppNotificationType.AutoParkingSpotSuccessful
import com.markoid.parky.core.presentation.notifications.AppNotificationType.Bluetooth
import com.markoid.parky.core.presentation.notifications.AppNotificationType.ReminderAlarm
import com.markoid.parky.core.presentation.notifications.NotificationConstants.AUTO_PARKING_CHANNEL_ID
import com.markoid.parky.core.presentation.notifications.NotificationConstants.BLUETOOTH_CHANNEL_ID
import com.markoid.parky.core.presentation.notifications.NotificationConstants.REMINDER_ALARM_CHANNEL_ID
import com.markoid.parky.core.presentation.notifications.NotificationConstants.regularVibrationPattern
import com.markoid.parky.core.presentation.notifications.NotificationConstants.urgentVibrationPattern

object NotificationChannelFactory {

    private const val REMINDER_ALARM_CHANNEL_DESCRIPTION = "Channel for displaying reminder alarm"

    private const val REMINDER_ALARM_CHANNEL_NAME = "ReminderAlarmChannel"

    private const val AUTO_PARKING_CHANNEL_DESCRIPTION =
        "Channel for displaying auto parking spot saved"

    private const val AUTO_PARKING_CHANNEL_NAME = "AutoParkingSpotChannel"

    private const val BLUETOOTH_CHANNEL_DESCRIPTION = "Channel for displaying bluetooth"

    private const val BLUETOOTH_CHANNEL_NAME = "BluetoothChannel"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getChannelByType(type: AppNotificationType): NotificationChannel = when (type) {
        AutoParkingSpotMissingData -> getAutoParkingSpotMissingChannel()
        AutoParkingSpotRequiresUserInteraction -> getAutoParkingSpotMissingChannel()
        AutoParkingSpotSuccessful -> getAutoParkingSpotSavedChannel()
        Bluetooth -> getBluetoothChannel()
        ReminderAlarm -> getReminderAlarmChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getReminderAlarmChannel(): NotificationChannel {
        val channel = NotificationChannel(
            REMINDER_ALARM_CHANNEL_ID,
            REMINDER_ALARM_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = REMINDER_ALARM_CHANNEL_DESCRIPTION
        channel.enableVibration(true)
        channel.vibrationPattern = urgentVibrationPattern
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAutoParkingSpotSavedChannel(): NotificationChannel {
        val channel = NotificationChannel(
            AUTO_PARKING_CHANNEL_ID,
            AUTO_PARKING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = AUTO_PARKING_CHANNEL_DESCRIPTION
        channel.enableVibration(true)
        channel.vibrationPattern = regularVibrationPattern
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAutoParkingSpotMissingChannel(): NotificationChannel {
        val channel = NotificationChannel(
            AUTO_PARKING_CHANNEL_ID,
            AUTO_PARKING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = AUTO_PARKING_CHANNEL_DESCRIPTION
        channel.enableVibration(true)
        channel.vibrationPattern = urgentVibrationPattern
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getBluetoothChannel(): NotificationChannel {
        val channel = NotificationChannel(
            BLUETOOTH_CHANNEL_ID,
            BLUETOOTH_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = BLUETOOTH_CHANNEL_DESCRIPTION
        channel.enableVibration(true)
        channel.vibrationPattern = regularVibrationPattern
        return channel
    }
}
