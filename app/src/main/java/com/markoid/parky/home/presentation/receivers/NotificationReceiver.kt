package com.markoid.parky.home.presentation.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.notificationManager

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager: NotificationManager = it.notificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                val name = "Alarm"
                val descriptionText = "Alarm details"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel("AlarmId", name, importance)
                mChannel.description = descriptionText
                notificationManager.createNotificationChannel(mChannel)
            }

            // Create the notification to be shown
            val mBuilder = NotificationCompat.Builder(it, "AlarmId")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Alarm")
                .setContentText("Remember to pick up your car")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // Generate an Id for each notification
            val id = System.currentTimeMillis() / 1000

            // Show a notification
            notificationManager.notify(id.toInt(), mBuilder.build())
        }
    }
}
