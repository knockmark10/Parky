package com.markoid.parky.core.presentation.managers

import android.app.NotificationChannel
import android.app.NotificationManager
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

private const val CHANNEL_DESCRIPTION = "Channel for displaying alarm notification"
private const val CHANNEL_ID = "AlarmChannel"
private const val CHANNEL_NAME = "ParkyChannel"
private const val REMINDER_NOTIFICATION_REQUEST_CODE = 1001

class NotificationManager(private val context: Context) {

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }

    private val notificationManager = context.notificationManager

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = CHANNEL_DESCRIPTION
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * This will be the notification displayed when the alarm goes off.
     */
    fun displayReminderAlarmNotification() {
        createNotificationChannel()
        val redirectIntent = Intent(context, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            REMINDER_NOTIFICATION_REQUEST_CODE,
            redirectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
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
}
