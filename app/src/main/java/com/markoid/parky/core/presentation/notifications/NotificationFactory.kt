package com.markoid.parky.core.presentation.notifications

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.getAttrColor
import com.markoid.parky.core.presentation.notifications.AppNotificationType.AutoParkingSpotMissingData
import com.markoid.parky.core.presentation.notifications.AppNotificationType.AutoParkingSpotSuccessful
import com.markoid.parky.core.presentation.notifications.AppNotificationType.Bluetooth
import com.markoid.parky.core.presentation.notifications.NotificationConstants.regularVibrationPattern
import com.markoid.parky.core.presentation.notifications.NotificationConstants.urgentVibrationPattern
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationFactory
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationIntentFactory: NotificationIntentFactory
) {

    fun getNotificationBuilderByType(
        type: AppNotificationType,
        channelId: String,
        incompleteRequest: ParkingSpotRequest? = null
    ): NotificationCompat.Builder = when (type) {
        AutoParkingSpotMissingData ->
            getAutoParkingMissingDataBuilder(channelId, incompleteRequest!!)
        AutoParkingSpotSuccessful -> getAutoParkingSuccessfulBuilder(channelId)
        Bluetooth -> getBluetoothBuilder(channelId)
        AppNotificationType.ReminderAlarm -> getReminderAlarmBuilder(channelId)
    }

    private fun getAutoParkingMissingDataBuilder(
        channelId: String,
        request: ParkingSpotRequest
    ): NotificationCompat.Builder {
        val pendingIntent: PendingIntent = notificationIntentFactory
            .getPendingIntentByNotificationType(AutoParkingSpotMissingData, request)
        return getCommonNotification(channelId, urgentVibrationPattern)
            .setSmallIcon(R.drawable.ic_error)
            .setContentTitle(context.getString(R.string.auto_parking_missing_data_notification_title))
            .setContentText(context.getString(R.string.auto_parking_missing_data_notification_message))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.auto_parking_missing_data_notification_message))
            )
            .setContentIntent(pendingIntent)
    }

    private fun getAutoParkingSuccessfulBuilder(channelId: String): NotificationCompat.Builder {
        val pendingIntent: PendingIntent = notificationIntentFactory
            .getPendingIntentByNotificationType(AutoParkingSpotSuccessful)
        return getCommonNotification(channelId, regularVibrationPattern)
            .setSmallIcon(R.drawable.ic_location)
            .setContentTitle(context.getString(R.string.auto_parking_spot_notification_title))
            .setContentText(context.getString(R.string.auto_parking_spot_notification_message))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.auto_parking_spot_notification_message))
            )
            .setContentIntent(pendingIntent)
    }

    private fun getBluetoothBuilder(channelId: String): NotificationCompat.Builder {
        val pendingIntent: PendingIntent = notificationIntentFactory
            .getPendingIntentByNotificationType(Bluetooth)
        return getCommonNotification(channelId, regularVibrationPattern)
            .setSmallIcon(R.drawable.ic_bluetooth)
            .setContentTitle(context.getString(R.string.bluetooth_notification_title))
            .setContentText(context.getString(R.string.bluetooth_notification_message))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.bluetooth_notification_message))
            )
            .setContentIntent(pendingIntent)
            .setOngoing(true)
    }

    private fun getReminderAlarmBuilder(channelId: String): NotificationCompat.Builder {
        val pendingIntent: PendingIntent = notificationIntentFactory
            .getPendingIntentByNotificationType(AutoParkingSpotSuccessful)
        return getCommonNotification(channelId, urgentVibrationPattern)
            .setSmallIcon(R.drawable.ic_parking)
            .setContentTitle(context.getString(R.string.alarm_notification_title))
            .setContentText(context.getString(R.string.alarm_notification_message))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(context.getString(R.string.alarm_notification_message))
            )
            .setContentIntent(pendingIntent)
    }

    private fun getCommonNotification(
        channelId: String,
        vibrationPattern: LongArray
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
        .setColor(context.getAttrColor(R.attr.colorPrimary))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(vibrationPattern)
}
