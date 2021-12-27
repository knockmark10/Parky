package com.markoid.parky.core.presentation.managers

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.notificationManager
import com.markoid.parky.core.presentation.extensions.resolveColor
import com.markoid.parky.home.presentation.activities.HomeActivity
import com.markoid.parky.home.presentation.enums.ChannelUsage
import com.markoid.parky.home.presentation.factories.NotificationChannelFactory
import com.markoid.parky.home.presentation.factories.NotificationChannelFactory.AUTO_PARKING_CHANNEL_ID
import com.markoid.parky.home.presentation.factories.NotificationChannelFactory.BLUETOOTH_CHANNEL_ID
import com.markoid.parky.home.presentation.factories.NotificationChannelFactory.REMINDER_ALARM_CHANNEL_ID

private const val REMINDER_NOTIFICATION_REQUEST_CODE = 1001
private const val AUTO_PARKING_REQUEST_CODE = 1002
private const val BLUETOOTH_REQUEST_CODE = 1003
const val GO_TO_ADD_PARKING = "go.to.add.parking"
const val GO_TO_USER_LOCATION = "go.to.user.location"
const val GO_TO_SETTINGS = "go.to.settings"

class NotificationManager(private val context: Context) {

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }

    private val notificationManager = context.notificationManager

    private fun createNotificationChannel(type: ChannelUsage) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannelFactory.getChannelForType(type)
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * This will be the notification displayed when the alarm goes off.
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.UserLocationFragment].
     */
    fun displayReminderAlarmNotification() {
        createNotificationChannel(ChannelUsage.ReminderAlarm)
        val redirectIntent = Intent(context, HomeActivity::class.java)
            .apply { action = GO_TO_USER_LOCATION }
        val pendingIntent = PendingIntent.getActivity(
            context,
            REMINDER_NOTIFICATION_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, REMINDER_ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_parking)
            .setColor(context.resolveColor(R.color.colorPrimary))
            .setContentTitle(context.getString(R.string.alarm_notification_title))
            .setContentText(context.getString(R.string.alarm_notification_message))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Generate an Id for each notification
        val id = System.currentTimeMillis() / 1000

        // Show a notification
        notificationManagerCompat.notify(id.toInt(), builder.build())
    }

    /**
     * This will be the notification displayed when parking spot has been saved automatically.
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.ParkingHistoryFragment].
     */
    fun displayAutoParkingSavedNotification() {
        createNotificationChannel(ChannelUsage.AutoParkingSpotSaving)
        val redirectIntent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            AUTO_PARKING_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, AUTO_PARKING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_location)
            .setColor(context.resolveColor(R.color.colorPrimary))
            .setContentTitle(context.getString(R.string.auto_parking_spot_notification_title))
            .setContentText(context.getString(R.string.auto_parking_spot_notification_message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Generate an Id for each notification
        val id = System.currentTimeMillis() / 1000

        // Show a notification
        notificationManagerCompat.notify(id.toInt(), builder.build())
    }

    /**
     * This will be the notification displayed when there is missing data for the auto parking spot.
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.AddParkingFragment].
     */
    fun displayAutoParkingMissingDataNotification() {
        createNotificationChannel(ChannelUsage.AutoParkingSpotSaving)
        val redirectIntent = Intent(context, HomeActivity::class.java)
            .apply { action = GO_TO_ADD_PARKING }
        val pendingIntent = PendingIntent.getActivity(
            context,
            AUTO_PARKING_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(context, AUTO_PARKING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_error)
            .setColor(context.resolveColor(R.color.colorPrimary))
            .setContentTitle(context.getString(R.string.auto_parking_missing_data_notification_title))
            .setContentText(context.getString(R.string.auto_parking_missing_data_notification_message))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Generate an Id for each notification
        val id = System.currentTimeMillis() / 1000

        // Show a notification
        notificationManagerCompat.notify(id.toInt(), builder.build())
    }

    /**
     * This will be the notification displayed when
     * [com.markoid.parky.home.presentation.services.BluetoothService] is running.
     *
     * When tapped, it should take the user to [com.markoid.parky.home.presentation.fragments.SettingsFragment].
     */
    fun getBluetoothServiceNotification(): Notification {
        createNotificationChannel(ChannelUsage.Bluetooth)
        val redirectIntent = Intent(context, HomeActivity::class.java)
            .apply { action = GO_TO_SETTINGS }
        val pendingIntent = PendingIntent.getActivity(
            context,
            BLUETOOTH_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(context, BLUETOOTH_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bluetooth)
            .setColor(context.resolveColor(R.color.colorPrimary))
            .setContentTitle(context.getString(R.string.bluetooth_notification_title))
            .setContentText(context.getString(R.string.bluetooth_notification_message))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }
}
