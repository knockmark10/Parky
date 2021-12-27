package com.markoid.parky.home.presentation.factories

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.markoid.parky.home.presentation.enums.ChannelUsage

object NotificationChannelFactory {

    private const val REMINDER_ALARM_CHANNEL_DESCRIPTION = "Channel for displaying reminder alarm"

    private const val REMINDER_ALARM_CHANNEL_NAME = "ReminderAlarmChannel"

    private const val AUTO_PARKING_CHANNEL_DESCRIPTION =
        "Channel for displaying auto parking spot saved"

    private const val AUTO_PARKING_CHANNEL_NAME = "AutoParkingSpotChannel"

    private const val BLUETOOTH_CHANNEL_DESCRIPTION = "Channel for displaying bluetooth"

    private const val BLUETOOTH_CHANNEL_NAME = "BluetoothChannel"

    const val REMINDER_ALARM_CHANNEL_ID = "ReminderAlarmId"

    const val AUTO_PARKING_CHANNEL_ID = "AutoParkingSpotId"

    const val BLUETOOTH_CHANNEL_ID = "BluetoothId"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getChannelForType(type: ChannelUsage): NotificationChannel = when (type) {
        ChannelUsage.ReminderAlarm -> getReminderAlarmChannel()
        ChannelUsage.AutoParkingSpotSaving -> getAutoParkingSpotChannel()
        ChannelUsage.Bluetooth -> getBluetoothChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getReminderAlarmChannel(): NotificationChannel {
        val channel = NotificationChannel(
            REMINDER_ALARM_CHANNEL_ID,
            REMINDER_ALARM_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = REMINDER_ALARM_CHANNEL_DESCRIPTION
        return channel
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAutoParkingSpotChannel(): NotificationChannel {
        val channel = NotificationChannel(
            AUTO_PARKING_CHANNEL_ID,
            AUTO_PARKING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = AUTO_PARKING_CHANNEL_DESCRIPTION
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
        return channel
    }
}
