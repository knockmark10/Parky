package com.markoid.parky.core.presentation.notifications

import android.app.Notification
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.markoid.parky.core.presentation.notifications.NotificationConstants.AUTO_PARKING_CHANNEL_ID
import com.markoid.parky.core.presentation.notifications.NotificationConstants.BLUETOOTH_CHANNEL_ID
import com.markoid.parky.core.presentation.notifications.NotificationConstants.REMINDER_ALARM_CHANNEL_ID
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import javax.inject.Inject

class AppNotificationManager @Inject constructor(
    private val notificationFactory: NotificationFactory,
    private val notificationManagerCompat: NotificationManagerCompat,
    private val notificationManager: NotificationManager
) {

    /**
     * This will be the notification displayed when the alarm goes off.
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.UserLocationFragment].
     */
    fun displayReminderAlarmNotification() {
        val channelId = createNotificationChannel(AppNotificationType.ReminderAlarm)
        val notificationBuilder = notificationFactory.getNotificationBuilderByType(
            type = AppNotificationType.ReminderAlarm,
            channelId = channelId
        )

        // Show a notification
        notificationManagerCompat.notify(generateNewId(), notificationBuilder.build())
    }

    /**
     * This will be the notification displayed when parking spot has been saved automatically.
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.ParkingHistoryFragment].
     */
    fun displayAutoParkingSavedNotification() {
        val channelId = createNotificationChannel(AppNotificationType.AutoParkingSpotSuccessful)
        val notificationBuilder = notificationFactory.getNotificationBuilderByType(
            type = AppNotificationType.AutoParkingSpotSuccessful,
            channelId = channelId
        )

        // Show a notification
        notificationManagerCompat.notify(generateNewId(), notificationBuilder.build())
    }

    /**
     * This will be the notification displayed when there is missing data for the auto parking spot.
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.AddParkingFragment].
     */
    fun displayAutoParkingMissingDataNotification(request: ParkingSpotRequest) {
        val channelId = createNotificationChannel(AppNotificationType.AutoParkingSpotMissingData)
        val notificationBuilder = notificationFactory.getNotificationBuilderByType(
            type = AppNotificationType.AutoParkingSpotMissingData,
            channelId = channelId,
            incompleteRequest = request
        )

        // Show a notification
        notificationManagerCompat.notify(generateNewId(), notificationBuilder.build())
    }

    /**
     * This will be the notification displayed when
     * [com.markoid.parky.home.presentation.services.BluetoothService] is running.
     *
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.SettingsFragment].
     */
    fun getBluetoothServiceNotification(): Notification {
        val channelId = createNotificationChannel(AppNotificationType.Bluetooth)
        return notificationFactory.getNotificationBuilderByType(
            type = AppNotificationType.Bluetooth,
            channelId = channelId
        ).build()
    }

    private fun createNotificationChannel(type: AppNotificationType): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannelFactory.getChannelByType(type)
            notificationManager.createNotificationChannel(channel)
        }
        return getChannelIdByType(type)
    }

    private fun getChannelIdByType(type: AppNotificationType): String = when (type) {
        AppNotificationType.Bluetooth -> BLUETOOTH_CHANNEL_ID
        AppNotificationType.ReminderAlarm -> REMINDER_ALARM_CHANNEL_ID
        AppNotificationType.AutoParkingSpotMissingData,
        AppNotificationType.AutoParkingSpotSuccessful -> AUTO_PARKING_CHANNEL_ID
    }

    /**
     * Generate an Id for each notification
     */
    private fun generateNewId(): Int = (System.currentTimeMillis() / 1000).toInt()
}
