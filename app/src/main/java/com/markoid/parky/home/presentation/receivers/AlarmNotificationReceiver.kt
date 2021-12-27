package com.markoid.parky.home.presentation.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.markoid.parky.core.presentation.managers.NotificationManager

class AlarmNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = NotificationManager(it)
            notificationManager.displayReminderAlarmNotification()
        }
    }
}
